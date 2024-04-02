package com.lkathary.kafka.consumer;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class KafkaConsumer {

    @KafkaListener(topics = "Kafka-cars", groupId = "consumer")
    public void kafkaListener(String message) {
        System.out.println("Record received: " + message);
    }
}
