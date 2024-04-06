package com.example.websockets;

import com.example.model.MappingResolver;
import com.example.model.SubscribeMessage;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.WebSocketMessage;
import org.springframework.web.reactive.socket.WebSocketSession;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


@Component
@Slf4j
@AllArgsConstructor
public class TradeHandler implements WebSocketHandler {

    private final MappingResolver mapper;
    private final TradeListener tradeListener;

    @Override
    public Mono<Void> handle(WebSocketSession webSocketSession) {
        Flux<String> bridge = webSocketSession
                .receive()
                .map(WebSocketMessage::getPayloadAsText)
                .map(mapper::mapStringToMessage)
                .flatMap(message -> register(webSocketSession, message))
                .log();


        return webSocketSession.send(bridge
                        .map(webSocketSession::textMessage)
                        .doFinally(signal -> tradeListener.unregister(webSocketSession))
                )
                .then();
    }

    private Flux<String> register(WebSocketSession webSocketSession, SubscribeMessage message) {
        return Flux.create(sink -> {
            tradeListener.register(webSocketSession, sink, message);
        });
    }

}
