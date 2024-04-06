package com.example.service;


import com.example.model.MappingResolver;
import com.example.websockets.TradeFlux;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
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
                                .map(tradesService::decorateTrade)
                                .map(tradesService::publishMessage)
//                                .doOnNext(tradeFlux::push) //TODO
                        )
                        .then()
        );
        result.subscribe();
    }



}
