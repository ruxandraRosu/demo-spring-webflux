package com.techconnect.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.techconnect.websockets.SubscriberInfo;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.kafka.core.reactive.ReactiveKafkaProducerTemplate;
import org.springframework.kafka.support.serializer.JsonSerializer;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.kafka.sender.SenderOptions;
import reactor.netty.http.client.HttpClient;
import reactor.netty.resources.ConnectionProvider;

import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


@Configuration
public class AppConfig {
    @Bean
    public WebClient webClient(@Value("${coinbase-rest.endpoint}") String endpoint) {
        HttpClient httpClient = HttpClient.create(ConnectionProvider.builder("myConnectionProvider")
                .maxConnections(60000)
                .maxIdleTime(Duration.ofSeconds(20))
                .maxLifeTime(Duration.ofSeconds(20))
                .pendingAcquireTimeout(Duration.ofSeconds(10))
                .pendingAcquireMaxCount(100000)
                .build());
        return WebClient.builder()
                .baseUrl(endpoint)
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .build();
    }

    @Bean
    public ReactiveKafkaProducerTemplate<String, String> reactiveKafkaProducer(KafkaProperties properties, @Value("${spring.kafka.producer.bootstrap-servers}") String bootstrapServers) {
        Map<String, Object> props = properties.buildProducerProperties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        props.put(ProducerConfig.REQUEST_TIMEOUT_MS_CONFIG, 500);
        props.put(ProducerConfig.DELIVERY_TIMEOUT_MS_CONFIG, 500);
        return new ReactiveKafkaProducerTemplate<>(SenderOptions.create(props));
    }

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }

    @Bean
    public Map<String, SubscriberInfo> subscribersMap() {
        return new ConcurrentHashMap<>();
    }

}
