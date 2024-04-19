package com.techconnect.model.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class SubscribeMessage {

    @JsonProperty("channel")
    private String channel;
    @JsonProperty("type")
    private String type;
    @JsonProperty("filters")
    private Map<String, List<String>> filters;


}
