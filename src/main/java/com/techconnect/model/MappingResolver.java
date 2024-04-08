package com.techconnect.model;

import com.techconnect.model.response.Trade;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.OffsetDateTime;

@Component
@AllArgsConstructor
@Slf4j
public record MappingResolver(ObjectMapper objectMapper) {

    public Match mapStringToMatch(String v) {
        try {
            return objectMapper.readValue(v, Match.class);
        } catch (JsonProcessingException e) {
            log.error("Error deserializing object", e);
            return null; //TODO
        }
    }

    public String writeTradeToString(Trade t) {
        String valueString;
        try {
            valueString = objectMapper.writeValueAsString(t);
        } catch (JsonProcessingException e) {
            return "";
        }
        return valueString;
    }

    public Trade mapMatchToTrade(Match match) {
        OffsetDateTime dateTime = OffsetDateTime.parse(match.getTime());
        String dateString = dateTime.toLocalDate().toString();
        String timeString = dateTime.toLocalTime().toString();
        return Trade.builder()
                .tradeId(match.getTradeId())
                .gridKey(match.getProductId())
                .takerOrderId(match.getTakerOrderId())
                .makerOrderId(match.getMakerOrderId())
                .sequence(match.getSequence())
                .size(match.getSize())
                .side(match.getSide())
                .datetime(match.getTime())
                .date(dateString)
                .time(timeString)
                .price(match.getPrice())
                .type(match.getType())
                .build();
    }

    public SubscribeMessage mapStringToMessage(String payloadString) {
        try {
            return objectMapper.readValue(payloadString, SubscribeMessage.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

}
