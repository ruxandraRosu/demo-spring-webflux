package com.example.websockets;

import com.example.model.response.Trade;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class MessageMatcher {

    public boolean matchProductType(Trade trade, Map<String, List<String>> filters) {
        List<String> productIdValues = filters.get("productId");
        if (productIdValues == null)
            return true;
        return productIdValues.contains(trade.getGridKey());
    }

    public boolean matchSide(Trade trade, Map<String, List<String>> filters) {
        List<String> side = filters.get("side");
        if (side == null)
            return true;
        return side.contains(trade.getSide());
    }

    public boolean matches(Trade trade, Map<String, List<String>> filters) {
        if (filters == null || filters.isEmpty())
            return true;

        return matchProductType(trade, filters) && matchSide(trade, filters);
    }
}
