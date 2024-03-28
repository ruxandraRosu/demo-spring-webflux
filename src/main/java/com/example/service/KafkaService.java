package com.example.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.reactive.ReactiveKafkaProducerTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@AllArgsConstructor
@Service
public class KafkaService {

    public static final String TOPIC_NAME = "topic-name";
    private ReactiveKafkaProducerTemplate reactiveKafkaProducer;

    public void sendMessage(String value){
        reactiveKafkaProducer.send(TOPIC_NAME, value)
                .doOnSuccess(senderResult -> log.info("sent {}", value))
                .subscribe();
    }
}
