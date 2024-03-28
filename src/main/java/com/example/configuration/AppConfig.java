package com.example.configuration;

import com.example.handlers.UserHandler;
import com.example.service.MyService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.kafka.core.reactive.ReactiveKafkaProducerTemplate;
import org.springframework.kafka.support.serializer.JsonSerializer;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;
import reactor.kafka.sender.SenderOptions;
import reactor.netty.http.client.HttpClient;
import reactor.netty.resources.ConnectionProvider;


import java.time.Duration;
import java.util.Map;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;


@Configuration
public class AppConfig {
    @Bean
    public RouterFunction<ServerResponse> routeHelloWorld() {
        return route(GET("/hello"),
                request -> ServerResponse.ok()
                        .contentType(MediaType.TEXT_PLAIN)
                        .body(Mono.just("Hello, Reactive World!"), String.class));
    }

    @Bean
    public RouterFunction<ServerResponse> userRoutes(UserHandler handler) {
        return route(GET("/people"), handler::getAllUsers)
                .andRoute(GET("/people/{id}"), handler::getUser)
                .andRoute(GET("/delay/{seconds}"), handler::delay)
                .andRoute(POST("/people"), handler::createUser);
    }

    @Bean
    public UserHandler userHandler(MyService myService) {
        return new UserHandler(myService);
    }

    @Bean
    public WebClient webClient() {
        HttpClient httpClient = HttpClient.create(ConnectionProvider.builder("myConnectionProvider")
                .maxConnections(60000)
                .maxIdleTime(Duration.ofSeconds(20))
                .maxLifeTime(Duration.ofSeconds(20))
                .pendingAcquireTimeout(Duration.ofSeconds(10))
                .pendingAcquireMaxCount(100000)
                .build());
        return WebClient.builder()
                .baseUrl("http://localhost:8084")
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .build();
    }

    @Bean
    public ReactiveKafkaProducerTemplate reactiveKafkaProducerTemplate(KafkaProperties properties) {
        Map<String, Object> props = properties.buildProducerProperties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);

        return new ReactiveKafkaProducerTemplate(SenderOptions.create(props));
    }
     @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }


}
