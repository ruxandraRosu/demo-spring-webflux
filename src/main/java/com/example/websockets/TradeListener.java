package com.example.websockets;

import com.example.model.SubscribeMessage;
import com.example.model.SubscriberInfo;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.socket.WebSocketSession;
import reactor.core.publisher.FluxSink;

import java.util.Map;

@Component
@AllArgsConstructor
@Slf4j
public class TradeListener {

    private final Map<String, SubscriberInfo> subscribersMap;

    public void register(WebSocketSession session, FluxSink<String> sink, SubscribeMessage message) {
        subscribersMap.put(session.getId(), new SubscriberInfo(message, sink));
    }

    public void unregister(WebSocketSession session) {
        log.info("Unregistering session {}", session.getId());
        subscribersMap.remove(session.getId());
    }
}
