package com.sarathy.demo.services;

import com.sarathy.demo.repository.UnMatchedCacheService;
import org.springframework.cache.annotation.CachePut;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.orm.jpa.persistenceunit.DefaultPersistenceUnitManager;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class Consumer {


    @KafkaListener(id = "batch-listener", topics = "unmatched")
    @CachePut(cacheNames ="messages")
    public void receive(@Payload List<String> messages,
                        @Header(KafkaHeaders.RECEIVED_PARTITION_ID) List<Integer> partitions,
                        @Header(KafkaHeaders.OFFSET) List<Long> offsets)throws InterruptedException {
        UnMatchedCacheService.getInstance().setFeeds(messages);
        for(int i=0;i<messages.size();i++)
        {
            System.out.println(messages.get(i));
        }
        Thread.sleep(3000);

    }

}