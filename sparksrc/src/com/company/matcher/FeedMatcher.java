package com.company.matcher;



import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import com.company.MyKafkaProducer;
import com.company.TradeFeed;
import com.company.TradeRecordFeed;
import com.datastax.spark.connector.japi.CassandraJavaUtil;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.streaming.Durations;
import org.apache.spark.streaming.api.java.JavaDStream;
import org.apache.spark.streaming.api.java.JavaInputDStream;
import org.apache.spark.streaming.api.java.JavaPairDStream;
import org.apache.spark.streaming.api.java.JavaStreamingContext;
import org.apache.spark.streaming.kafka010.ConsumerStrategies;
import org.apache.spark.streaming.kafka010.KafkaUtils;
import org.apache.spark.streaming.kafka010.LocationStrategies;
import scala.Tuple2;


public class FeedMatcher
{

    private static final String MYSQL_USERNAME = "root";
    private static final String MYSQL_PWD = "Dare@123";
    private static final String MYSQL_CONNECTION_URL = "jdbc:mysql://127.0.0.1:3306/feeds?"+"autoReconnect=true&useSSL=false";

    public static void main(String[] args) throws InterruptedException {
        // write your code here

        Logger.getLogger("org")
                .setLevel(Level.OFF);
        Logger.getLogger("akka")
                .setLevel(Level.OFF);


        Properties sqlProps=new Properties();
        sqlProps.put("user",MYSQL_USERNAME);
        sqlProps.put("password",MYSQL_PWD);


        Map<String, Object> kafkaParams = new HashMap<>();
        kafkaParams.put("bootstrap.servers", "localhost:9092");
        kafkaParams.put("key.deserializer", StringDeserializer.class);
        kafkaParams.put("value.deserializer", StringDeserializer.class);
        kafkaParams.put("group.id", "use_a_separate_group_id_for_each_stream");
        kafkaParams.put("auto.offset.reset", "latest");
        kafkaParams.put("enable.auto.commit", false);

        Collection<String> topics = Arrays.asList("orderfeed","unmatched");

        System.out.println("Listening to topic :"+topics);
        SparkSession spark = SparkSession.builder().master("local[*]")
                .appName("test").getOrCreate();
        JavaSparkContext sc = new JavaSparkContext(spark.sparkContext());


        int numStreams = 5;
        List<JavaPairDStream<String, String>> kafkaStreams = new ArrayList<JavaPairDStream<String, String>>(numStreams);

        JavaStreamingContext streamingContext = new JavaStreamingContext(sc, Durations.seconds(3));

        JavaInputDStream<ConsumerRecord<String, String>> messages = KafkaUtils.createDirectStream(streamingContext, LocationStrategies.PreferConsistent(), ConsumerStrategies.<String, String> Subscribe(topics, kafkaParams));

        JavaPairDStream<String, String> results = messages.mapToPair(record -> new Tuple2<>(record.key(), record.value()));

        JavaDStream<String> lines = results.map(tuple2 -> tuple2._2());

        lines.print();

        lines.foreachRDD((rdd,time)->
        {
           //  SparkSession spark = JavaSparkSessionSingleton.getInstance(rdd.context().getConf());
            JavaRDD<TradeFeed> rowRDD = rdd.map(word -> {
                //  System.out.println(word);
                TradeFeed feed = new TradeFeed();
                if ((!word.trim().isEmpty() || word != null) && (word.contains("BUY") || word.contains("SELL"))) {
                    String[] arr = word.split(",");
                    if (arr[0] != null) {
                        feed.setCompany(arr[2]);
                        feed.setPartyName(arr[0]);
                        feed.setTradeType(arr[1]);
                        feed.setTradePrice(arr[3]);
                    }
                }

                return feed;
            });
            rowRDD.count();
            Dataset<Row> wordsDataFrame = spark.createDataFrame(rowRDD, TradeFeed.class);
            wordsDataFrame.show();
            wordsDataFrame.createOrReplaceTempView("buy");
            Dataset<Row> buyDF = spark.sql("select company as buycompany,partyName as buyParty,tradePrice as buyPrice,tradeType as buyType from buy where tradeType like '%BUY%'"
            );
            buyDF.show();
            Dataset<Row> sellDF = spark.sql("select *   from buy where tradeType like '%SELL%'");
            sellDF.show();
            LinkedHashMap<String, Integer> sellMap = new LinkedHashMap<>();
            Row[] sellRow = (Row[]) sellDF.collect();
            Row[] buyRow = (Row[]) buyDF.collect();

            for (int i = 0; i < sellRow.length; i++) {
                String key = sellRow[i].getAs(0).toString() + sellRow[i].getAs(2).toString();
                System.out.println(key);
                if (sellMap.containsKey(key)) {
                    int count = sellMap.get(key);
                    sellMap.put(key, count + 1);
                } else {
                    sellMap.put(key, 1);
                }
            }

            Set<String> keys = sellMap.keySet();

            /*
             * Convert it to xan ArrayList
             */
            List<String> listKeys = new ArrayList<String>(keys);


            System.out.println("SELL MAP----->");

            for (String key : sellMap.keySet()) {
                System.out.println(key + "," + sellMap.get(key));
            }
            ArrayList<TradeRecordFeed> recordFeeds = new ArrayList<>();
            ArrayList<String>unMatchedFeeds=new ArrayList<>();
            StringBuilder str=new StringBuilder();
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
            for (int i = 0; i < buyRow.length; i++) {
                String key = buyRow[i].getAs(0).toString() + buyRow[i].getAs(2).toString();
                //str.append("partha\n");
                if (sellMap.containsKey(key) && sellMap.get(key) > 0) {
                    TradeRecordFeed record = new TradeRecordFeed();
                    record.setBuyer(buyRow[i].getAs(1).toString());
                    record.setPrice(Float.parseFloat(buyRow[i].getAs(2).toString()));
                    record.setSeller(sellRow[i].getAs(1).toString());
                    record.setTradedate(LocalDateTime.now().toLocalDate().toString());
                    record.setStock(buyRow[i].getAs(0).toString());
                    // record.setTradeid(i+"-"+ LocalDateTime.now().toString());
                    int pos = listKeys.indexOf(key);

                    System.out.println(buyRow[i].getAs(0).toString() + "," + buyRow[i].getAs(1).toString() + "," + buyRow[i].getAs(2).toString() + "," + buyRow[i].getAs(3).toString() +
                            sellRow[pos].getAs(0).toString() + "," + sellRow[pos].getAs(1).toString() + "," + sellRow[pos].getAs(2).toString() + "," + sellRow[pos].getAs(3).toString()
                    );
                    int value = sellMap.get(key);
                    sellMap.put(key, value - 1);
                    recordFeeds.add(record);
                } else {
                  //  MyKafkaProducer.getProducer().send(new ProducerRecord("unmatched",
                            unMatchedFeeds.add(buyRow[i].getAs(1).toString()+","+buyRow[i].getAs(3).toString()+","+buyRow[i].getAs(0).toString()+","+buyRow[i].getAs(2).toString()+"\n");
                    System.out.println("Not Matched:" + buyRow[i].getAs(1).toString() + "," + buyRow[i].getAs(3).toString() + "," + buyRow[i].getAs(0).toString() + "," + buyRow[i].getAs(2).toString());

                }
            }
            int iterator = 0;
            for (Map.Entry<String, Integer> entry : sellMap.entrySet()) {
                System.out.println(entry.getKey() + "," + entry.getValue());
                if (entry.getValue() > 0) {
                    for (int i = 0; i < entry.getValue(); i++) {
                        MyKafkaProducer.getProducer().send(new ProducerRecord(unMatchedFeeds.add(sellRow[iterator].getAs(1).toString() + "," + sellRow[iterator].getAs(3).toString() + "," + sellRow[iterator].getAs(0).toString() + "," + sellRow[iterator].getAs(2).toString()+"\n");
                        System.out.println("Not Matched:" + sellRow[iterator].getAs(0).toString() + "," + sellRow[iterator].getAs(1).toString() + "," + sellRow[iterator].getAs(2).toString() + "," + sellRow[iterator].getAs(3).toString());

                    }
                }
                iterator++;
            }

            MyKafkaProducer.getProducer().send(new ProducerRecord("unmatched",unMatchedFeeds.toArray()));



            //Persisting recors
            JavaRDD<TradeRecordFeed> recordrdd = streamingContext.sparkContext()
                    .parallelize(recordFeeds);
            System.out.println(recordrdd.count());
            Dataset<Row> tradedf = spark.createDataFrame(recordrdd, TradeRecordFeed.class);

            tradedf.write().mode("append").jdbc(MYSQL_CONNECTION_URL,"traderecord",sqlProps);
//                    CassandraJavaUtil.javaFunctions(recordrdd).writerBuilder("mykeyspace", "traderecord", CassandraJavaUtil.mapToRow(TradeRecordFeed.class))
//                                .saveToCassandra();
//

        });


        streamingContext.start();
        streamingContext.awaitTermination();
    }
}


class JavaSparkSessionSingleton {
    private static transient SparkSession instance = null;

    public static SparkSession getInstance(SparkConf sparkConf) {
        if (instance == null) {
            instance = SparkSession
                    .builder()
                    .config(sparkConf)
                    .getOrCreate();
        }
        return instance;
    }
}

