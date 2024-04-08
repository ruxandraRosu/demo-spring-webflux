package com.techconnect.websockets;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.HandlerMapping;
import org.springframework.web.reactive.config.WebFluxConfigurer;
import org.springframework.web.reactive.handler.SimpleUrlHandlerMapping;
import org.springframework.web.reactive.socket.WebSocketHandler;

import java.util.HashMap;
import java.util.Map;

@Configuration
@RequiredArgsConstructor
@Slf4j
@ConditionalOnProperty(prefix = "application.websockets", name = "enabled", havingValue = "true")
public class WebSocketConfiguration implements WebFluxConfigurer {

    @Bean
    public HandlerMapping handlerMapping(TradeHandler tradeHandler) {
        Map<String, WebSocketHandler> map = new HashMap<>();
        map.put("feed/trades", tradeHandler);
        return new SimpleUrlHandlerMapping(map);
    }


}
