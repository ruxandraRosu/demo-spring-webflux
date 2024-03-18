package com.example.configuration;

import com.example.handlers.UserHandler;
import com.example.service.MyService;
import org.springframework.boot.web.client.ClientHttpRequestFactories;
import org.springframework.boot.web.client.ClientHttpRequestFactorySettings;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;
import reactor.netty.resources.ConnectionProvider;


import java.time.Duration;

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
//        ClientHttpRequestFactorySettings settings = ClientHttpRequestFactorySettings.DEFAULTS
//                .withReadTimeout(Duration.ofMinutes(12))
//                ;
//        ClientHttpRequestFactory requestFactory = ClientHttpRequestFactories.get(settings);
//        requestFactory.
//        HttpClient httpClient = HttpClient.create()
//                .responseTimeout(Duration.ofSeconds(8))
//                .con
//                ;

        HttpClient httpClient = HttpClient.create(ConnectionProvider.builder("myConnectionProvider")
                .maxConnections(60000)
                .maxIdleTime(Duration.ofSeconds(20))
                .maxLifeTime(Duration.ofSeconds(20))
                .pendingAcquireTimeout(Duration.ofSeconds(4))
                .pendingAcquireMaxCount(100000)
                .build());
        return WebClient.builder()
                .baseUrl("http://localhost:8084")
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .build();
    }
//    @Bean
//    public WebClient.Builder  webClientCustomizer() {
//        webClientBuilder.baseUrl("https://example.org")
//
//    }
}
