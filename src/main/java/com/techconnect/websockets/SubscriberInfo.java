package com.techconnect.websockets;

import com.techconnect.model.request.SubscribeMessage;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import reactor.core.publisher.FluxSink;

@Getter
@Setter
@AllArgsConstructor
public class SubscriberInfo {
    private SubscribeMessage message;
    private FluxSink sink;
}
