package com.example.model.response;

import com.example.model.Product;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Trade {

    private String gridKey;
    private String price;
    private String type;
    private String size;
    private String side;
    private String datetime;
    private String time;
    private String date;
    private String makerOrderId;
    private String takerOrderId;
    private String tradeId;
    private Long sequence;
    private String displayName;
    private String base;

    public static class TradeBuilder {

        public TradeBuilder product(Product product) {
            this.base = product.getBaseCurrency();
            this.displayName = product.getDisplayName();
            return this;
        }

        public TradeBuilder trade(Trade trade2) {
            this.gridKey = trade2.getGridKey();
            this.price = trade2.getPrice();
            this.type = trade2.getType();
            this.size = trade2.getSize();
            this.side = trade2.getSide();
            this.datetime = trade2.getDatetime();
            this.time = trade2.getTime();
            this.date = trade2.getDate();
            this.makerOrderId = trade2.getMakerOrderId();
            this.takerOrderId = trade2.getTakerOrderId();
            this.tradeId = trade2.getTradeId();
            this.sequence = trade2.getSequence();
            return this;
        }

    }

}
