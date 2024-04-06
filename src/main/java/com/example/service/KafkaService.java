package com.example.service;

import com.example.model.response.Trade;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.reactive.ReactiveKafkaProducerTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Slf4j
@AllArgsConstructor
@Service
public class KafkaService {

    public static final String TOPIC_NAME = "topic-name";
    public static final String TOPIC_NAME_TRADES = "topic-trades";
    private ReactiveKafkaProducerTemplate<String, String> reactiveKafkaProducer;
    private ObjectMapper mapper;

    public Mono sendMessage(Mono<Trade> trade) {

        return trade.map(this::getValueAsString)
                .flatMap(message -> reactiveKafkaProducer.send(TOPIC_NAME_TRADES, message))
                .doOnNext(message -> log.info("After kafka" + message.toString()));
    }

    private String getValueAsString(Trade trade) {
        try {
            return mapper.writeValueAsString(trade);
        } catch (JsonProcessingException e) {
            log.error("Unable to serialize object", e);
            return "";
        }
    }

}
