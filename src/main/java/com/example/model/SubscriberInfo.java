package com.example.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;

@Getter
@Setter
@AllArgsConstructor
public class SubscriberInfo {
    private SubscribeMessage message;
    private FluxSink sink;
}
