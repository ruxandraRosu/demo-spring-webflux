package com.techconnect.service;


import com.techconnect.model.MappingResolver;
import com.techconnect.websockets.TradeFlux;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.socket.WebSocketMessage;
import org.springframework.web.reactive.socket.client.ReactorNettyWebSocketClient;
import org.springframework.web.reactive.socket.client.WebSocketClient;
import reactor.core.publisher.Mono;

import javax.annotation.PostConstruct;
import java.net.URI;

@Service
@Slf4j
@RequiredArgsConstructor
@ConditionalOnProperty(prefix = "application.websockets", name = "enabled", havingValue = "true")
public class MatchService {

    @Value("${coinbase.endpoint}")
    private String url;
    @Value("${coinbase.matcher-message}")
    private String matcherMessage;
    private final MappingResolver mappingResolver;
    private final TradeFlux tradeFlux;
    private final TradesService tradesService;


    @PostConstruct
    public void afterInit() {
        WebSocketClient client = new ReactorNettyWebSocketClient();
        Mono<Void> result = client.execute(URI.create(url),
                (session) -> session.send(Mono.just(session.textMessage(matcherMessage)))
                        .ignoreElement()
                        .thenMany(session.receive()
                                .map(WebSocketMessage::getPayloadAsText)
                                .log()
                                .filter(v -> !v.contains("subscriptions"))
                                .mapNotNull(mappingResolver::mapStringToMatch)
                                .map(mappingResolver::mapMatchToTrade)
                                .flatMap(tradesService::decorateTrade)
                                .flatMap(trade -> {
                                    tradesService.publishMessage(trade);
                                    return Mono.just(trade);
                                })
                                .doOnNext(tradeFlux::push)
                        )
                        .then()
        );
        result.subscribe();
    }



}
