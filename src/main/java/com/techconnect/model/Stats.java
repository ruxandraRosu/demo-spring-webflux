package com.techconnect.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class Stats {

    @JsonProperty("open")
    private String open;
    @JsonProperty("high")
    private String high;
    @JsonProperty("low")
    private String low;
    @JsonProperty("volume")
    private String volume;


}
