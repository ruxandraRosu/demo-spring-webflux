package com.techconnect.websockets;

import com.techconnect.model.MappingResolver;
import com.techconnect.model.response.Trade;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@Slf4j
@AllArgsConstructor
@ConditionalOnProperty(prefix = "application.websockets", name = "enabled", havingValue = "true")
public class TradeFlux {

   private final MappingResolver mapper;
   private final MessageMatcher matcher;
   private final Map<String, SubscriberInfo> subscribersMap;

    public void push(Trade trade) {
        subscribersMap.entrySet().stream()
                .filter(e -> matcher.matches(trade, subscribersMap.get(e.getKey()).getMessage().getFilters()))
                .forEach(e -> e.getValue().getSink().next(mapper.writeTradeToString(trade)));

    }


}
