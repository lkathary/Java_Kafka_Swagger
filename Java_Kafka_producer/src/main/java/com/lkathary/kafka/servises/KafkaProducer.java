package com.lkathary.kafka.servises;

import lombok.AllArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
@AllArgsConstructor
public class KafkaProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;

    public void kafkaSender(String msg) {
//        kafkaTemplate.send("Kafka-cars", msg);
        CompletableFuture<SendResult<String, String>> future = kafkaTemplate.send("Kafka-cars", msg);
        future.whenComplete((result, ex) -> {
            if (ex != null) {
                System.err.println("Alarm!");
                ex.printStackTrace();
            } else {
                System.out.println("Message sent");
            }
        });
    }
}
