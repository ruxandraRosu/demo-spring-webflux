package com.example.service;

import com.example.dto.MyDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class MyService {

    private final WebClient webClient;
    private final KafkaService kafkaService;
    private ObjectMapper mapper;

    public void callExternalService(int seconds) {

        webClient.get()
                .uri("/block/" + seconds)
                .retrieve()
                .toBodilessEntity()
                .timeout(Duration.ofSeconds(35))
                .subscribe((response) -> log.info("{} on {}", response.getStatusCode(), Thread.currentThread()));
    }

    public void publishMessage(MyDto message) {
        String value = "";

        try {
            value = mapper.writeValueAsString(message);
        } catch (JsonProcessingException e) {
            log.error("Error serializing object", e);
        }
        kafkaService.sendMessage(value);
    }


    public void doWork(List<MyDto> list) {
        int sum = 0; //TODO
        Flux.range(1, 10)
                .map(i -> new DigestUtils("SHA-256").digestAsHex(i + list.get(0).toString()))
                .subscribe(
                        v -> log.info("Hash {}", v)
                );

    }
}
