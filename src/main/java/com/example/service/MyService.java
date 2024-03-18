package com.example.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.Duration;

@Service
@AllArgsConstructor
@Slf4j
public class MyService {

    private final WebClient webClient;

    public void callExternalService(int seconds) {

         webClient.get()
                .uri("/block/" + seconds)
                .retrieve()
                .toBodilessEntity()
                .timeout(Duration.ofSeconds(35))
                .subscribe((response) -> log.info("{} on {}", response.getStatusCode(), Thread.currentThread()));
    }


    public void doWork() {

    }
}
