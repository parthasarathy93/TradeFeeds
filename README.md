# TradeFeeds


# Prerequisites:

Java Version -1.8
Kafka 
Apache Spark
Mysql 


# Solution:
The Solution consists of three services 
1. App Service
2. Feeds Service
3. Matching Service

Service discovery happens between services through properties file


# App Service:

The App Service is a spring boot application which exposes the api for users. The apis are

1. Publish Message:(/kafka/publish)
    This endpoint is used for publishing orderfeeds into kafka
    
2. Get TradeRecords
    
    Example 1: http://localhost:9091/trades/all
    
    This would give all the existing traderecords in the system
    
 Sample Output:
    
      {"content":[{"id":21,"buyer":"Party C","seller":"Party A","stock":" INFY","price":600,"tradedate":"2020-09-10"},{"id":20,"buyer":"Party B","seller":"Party A","stock":" IBM","price":110,"tradedate":"2020-09-10"},{"id":19,"buyer":"Party B","seller":"Party A","stock":" IBM","price":110,"tradedate":"2020-09-10"},{"id":18,"buyer":"Party C","seller":"Party A","stock":" INFY","price":600,"tradedate":"2020-09-10"},{"id":17,"buyer":"Party C","seller":"Party A","stock":" INFY","price":600,"tradedate":"2020-09-10"},{"id":16,"buyer":"Party B","seller":"Party A","stock":" IBM","price":110,"tradedate":"2020-09-10"},{"id":15,"buyer":"Party B","seller":"Party A","stock":" IBM","price":110,"tradedate":"2020-09-10"},{"id":14,"buyer":"Party C","seller":"Party A","stock":" INFY","price":600,"tradedate":"2020-09-10"},{"id":13,"buyer":"Party C","seller":"Party A","stock":" INFY","price":600,"tradedate":"2020-09-10"},{"id":12,"buyer":"Party B","seller":"Party A","stock":" IBM","price":110,"tradedate":"2020-09-10"},{"id":11,"buyer":"Party B","seller":"Party A","stock":" IBM","price":110,"tradedate":"2020-09-10"},{"id":10,"buyer":"Party C","seller":"Party A","stock":" INFY","price":600,"tradedate":"2020-09-10"},{"id":9,"buyer":"Party C","seller":"Party A","stock":" INFY","price":600,"tradedate":"2020-09-10"},{"id":8,"buyer":"Party B","seller":"Party A","stock":" IBM","price":110,"tradedate":"2020-09-10"},{"id":7,"buyer":"Party C","seller":"Party A","stock":" IBM","price":110,"tradedate":"2020-09-10"},{"id":6,"buyer":"Party A","seller":"Party A","stock":" GOOG","price":500,"tradedate":"2020-09-10"},{"id":5,"buyer":"Party B","seller":"Party A","stock":" IBM","price":110,"tradedate":"2020-09-10"},{"id":4,"buyer":"Party C","seller":"Party A","stock":" INFY","price":600,"tradedate":"2020-09-10"},{"id":3,"buyer":"Party B","seller":"Party A","stock":" IBM","price":110,"tradedate":"2020-09-10"},{"id":2,"buyer":"Party C","seller":"Party A","stock":" INFY","price":600,"tradedate":"2020-09-09"},{"id":1,"buyer":"Party B","seller":"Party A","stock":" IBM","price":110,"tradedate":"2020-09-09"}],"pageable":{"sort":{"sorted":true,"unsorted":false,"empty":false},"offset":0,"pageNumber":0,"pageSize":100,"unpaged":false,"paged":true},"last":true,"totalPages":1,"totalElements":21,"numberOfElements":21,"first":true,"sort":{"sorted":true,"unsorted":false,"empty":false},"size":100,"number":0,"empty":false}
      
      
      
      Example 2: http://localhost:9091/trades?buyer=Party B&stock=IBM&tradedate=2020-09-10. 
      
      We can even apply filters for the resultset  based on 
       buyer
       seller
       stock
       tradedte
      
      {"content":[{"id":1,"buyer":"Party B","seller":"Party A","stock":" IBM","price":110,"tradedate":"2020-09-10"},{"id":3,"buyer":"Party B","seller":"Party A","stock":" IBM","price":110,"tradedate":"2020-09-10"},{"id":5,"buyer":"Party B","seller":"Party A","stock":" IBM","price":110,"tradedate":"2020-09-10"},{"id":8,"buyer":"Party B","seller":"Party A","stock":" IBM","price":110,"tradedate":"2020-09-10"},{"id":11,"buyer":"Party B","seller":"Party A","stock":" IBM","price":110,"tradedate":"2020-09-10"},{"id":12,"buyer":"Party B","seller":"Party A","stock":" IBM","price":110,"tradedate":"2020-09-10"},{"id":15,"buyer":"Party B","seller":"Party A","stock":" IBM","price":110,"tradedate":"2020-09-10"},{"id":16,"buyer":"Party B","seller":"Party A","stock":" IBM","price":110,"tradedate":"2020-09-10"},{"id":19,"buyer":"Party B","seller":"Party A","stock":" IBM","price":110,"tradedate":"2020-09-10"},{"id":20,"buyer":"Party B","seller":"Party A","stock":" IBM","price":110,"tradedate":"2020-09-10"}],"pageable":{"sort":{"sorted":true,"unsorted":false,"empty":false},"offset":0,"pageNumber":0,"pageSize":10,"unpaged":false,"paged":true},"last":true,"totalPages":1,"totalElements":10,"numberOfElements":10,"first":true,"sort":{"sorted":true,"unsorted":false,"empty":false},"size":10,"number":0,"empty":false}
      
      
      
  3: Get UnMatched Feed:
  
      
  Example 1: http://localhost:9091/feeds
       
      {"content":[{"Party C, BUY, IBM, 110","Party A, SELL, GOOG, 500","Party C, BUY, IBM, 110","Party A, SELL, GOOG, 500","Party C, BUY, IBM, 110","Party A, SELL, GOOG, 500","Party C, BUY, IBM, 110","Party A, SELL, GOOG, 500","Party C, BUY, IBM, 110","Party A, SELL, GOOG, 500","Party C, BUY, IBM, 110","Party A, SELL, GOOG, 500","Party C, BUY, IBM, 110","Party A, SELL, GOOG, 500","Party C, BUY, IBM, 110","Party A, SELL, GOOG, 500","Party C, BUY, IBM, 110","Party A, SELL, GOOG, 500","Party C, BUY, IBM, 110","Party A, SELL, GOOG, 500","Party C, BUY, IBM, 110","Party A, SELL, GOOG, 500","Party C, BUY, IBM, 110","Party A, SELL, GOOG, 500","Party C, BUY, IBM, 110","Party A, SELL, GOOG, 500","Party C, BUY, IBM, 110","Party A, SELL, GOOG, 500","Party C, BUY, IBM, 110","Party A, SELL, GOOG, 500","Party C, BUY, IBM, 110","Party A, SELL, GOOG, 500","Party C, BUY, IBM, 110","Party A, SELL, GOOG, 500","Party C, BUY, IBM, 110","Party A, SELL, GOOG, 500","Party C, BUY, IBM, 110","Party A, SELL, GOOG, 500","Party C, BUY, IBM, 110","Party A, SELL, GOOG, 500","Party C, BUY, IBM, 110","Party A, SELL, GOOG, 500","Party C, BUY, IBM, 110","Party A, SELL, GOOG, 500","Party C, BUY, IBM, 110","Party A, SELL, GOOG, 500","Party C, BUY, IBM, 110","Party A, SELL, GOOG, 500","Party C, BUY, IBM, 110","Party A, SELL, GOOG, 500","Party C, BUY, IBM, 110","Party A, SELL, GOOG, 500","Party C, BUY, IBM, 110","Party A, SELL, GOOG, 500","Party C, BUY, IBM, 110","Party A, SELL, GOOG, 500","Party C, BUY, IBM, 110","Party A, SELL, GOOG, 500","Party C, BUY, IBM, 110","Party A, SELL, GOOG, 500","Party C, BUY, IBM, 110","Party A, SELL, GOOG, 500","Party C, BUY, IBM, 110","Party A, SELL, GOOG, 500","Party C, BUY, IBM, 110","Party A, SELL, GOOG, 500","Party C, BUY, IBM, 110","Party A, SELL, GOOG, 500","Party C, BUY, IBM, 110","Party A, SELL, GOOG, 500","Party C, BUY, IBM, 110","Party A, SELL, GOOG, 500","Party C, BUY, IBM, 110","Party A, SELL, GOOG, 500","Party C, BUY, IBM, 110","Party A, SELL, GOOG, 500","Party C, BUY, IBM, 110","Party A, SELL, GOOG, 500","Party C, BUY, IBM, 110","Party A, SELL, GOOG, 500","Party C, BUY, IBM, 110","Party A, SELL, GOOG, 500","Party C, BUY, IBM, 110","Party A, SELL, GOOG, 500","Party C, BUY, IBM, 110","Party A, SELL, GOOG, 500","Party C, BUY, IBM, 110","Party A, SELL, GOOG, 500","Party C, BUY, IBM, 110","Party A, SELL, GOOG, 500","Party C, BUY, IBM, 110","Party A, SELL, GOOG, 500","Party C, BUY, IBM, 110","Party A, SELL, GOOG, 500","Party C, BUY, IBM, 110","Party A, SELL, GOOG, 500","Party C, BUY, IBM, 110","Party A, SELL, GOOG, 500","Party C, BUY, IBM, 110","Party A, SELL, GOOG, 500"}],"pageable":{"sort":{"sorted":true,"unsorted":false,"empty":false},"offset":0,"pageNumber":0,"pageSize":100,"unpaged":false,"paged":true},"last":false,"totalPages":3,"totalElements":228,"numberOfElements":100,"first":true,"sort":{"sorted":true,"unsorted":false,"empty":false},"size":100,"number":0,"empty":false}
      
      
   Example 2: https://localhost:9091/feeds?stock=IBM&price=110
      
      
      
      
      
      
      
      
      
      
      
      
      
      
      
      
      
      
      
      
      
      
      
   How app shows unmatcheddata?
      
      Every time a feed is produced under unmatched topic  is a fresh list and updated list , we dont need to care for the previous ones,We have to make sure every       time we consume we have a fresh list of unmatched records.
      
      A Kafka consumer Program runs in the app , where it listens to the kafka consumer for every 3seconds and maintains that data in a cache.
      Since we have two consumer groups , the offsets that they read from will be almost same
      For simplicity sake,we have used a singleton instance of a class with a list has the cache which gets directly replaced after each consumption
      
      User's api requests are served from this cache
      
      
      
        
 Note:  
      
      All the api results are paginated , the key content has the data a
       Pagenumber-> currentpageno
       totalpages-> total number of pages
       totalElements-> Over all resultset count
       numberOfElements-> Current resultset count
       first-> isfirst page
       empty-> Says whether the data is empty or not
       
       
       
       
  # FEEDS SERVICE:
        
        The feeds service is a kafka cluster with zookeeper bundled in for fault tolerance. We have created two topics 
        1. orderfeed-> real order feed 
        2. unmatched-> unmatched feed after every batch processing
        
        There are two consumer groups one for the matching service and other one for the kafka consumer in the app side
        
        
        Kafka Version used:  kafka_2.12-2.1.0 -https://archive.apache.org/dist/kafka/2.1.0/kafka_2.12-2.1.0.tgz
        
        
        
 # MATCHING SERVICE: [ APACHE SPARK- VERSION 2.3.0]
        
        The matching service is performed by apache spark. It consumes streams from both the topics and unions them and perform the matching every 2 seconds.
        We can increase the batch interval based on our requirements. 
        After every batch, the spark will do two things
        
        1. Pushing the traderecords to DB
        2. Pushing the unmatched into unmatched topic for the next consumption
        
        
 # Order for how to run the services:
 
 
   # Start Feeds Service:
     Unzip the kafka  and run 
      
      To Start Zookeeper.  - bin/zookeeper-server-start.sh config/zookeeper.properties
      
      To Start Kafka Server -bin/kafka-server-start.sh config/server.properties
      
  Create topics orderfeed,unmatched
      
        bin/kafka-topics.sh --create --zookeeper localhost:2181 --replication-factor 1 --partitions 1 --topic orderfeed
        bin/kafka-topics.sh --create --zookeeper localhost:2181 --replication-factor 1 --partitions 1 --topic unmatched
        
        
   # Start Matching Service:
      Unzip the spark 
      
      Add dependency files
      
      
      Run the command :  ./bin/spark-submit --name "feeds" --master local[4] --class com.company.matcher.FeedMatcher jars/matcher.jar
      
      
   # Start spring boot app
   
   
      
      
      
      
      
      
      
      
        
    
    
        
        
        
        
     
     
        
        
        
        
        
        
        
        
        
        
        
        
     
        
        
        
        
        
      
        
        
        
 
       
       
      
      
      
      
   
    
   
  
     
   
    


