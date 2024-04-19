package com.techconnect.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class Match {
    @JsonProperty("type")
    private String type;
    @JsonProperty("trade_id")
    private String tradeId;
    @JsonProperty("maker_order_id")
    private String makerOrderId;
    @JsonProperty("taker_order_id")
    private String takerOrderId;
    @JsonProperty("side")
    private String side;
    @JsonProperty("size")
    private String size;
    @JsonProperty("price")
    private String price;
    @JsonProperty("product_id")
    private String productId;
    @JsonProperty("sequence")
    private Long sequence;
    @JsonProperty("time")
    private String time;

}
