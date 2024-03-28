package com.example.handlers;

import com.example.model.User;
import com.example.service.MyService;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
public class UserHandler {

    private final Map<String, User> userMap = new HashMap<>();

    @NonNull
    private MyService myService;

    public Mono<ServerResponse> getAllUsers(ServerRequest request) {
        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(Flux.fromIterable(userMap.values()), User.class);
    }

    public Mono<ServerResponse> getUser(ServerRequest request) {
        String userId = request.pathVariable("id");
        User user = userMap.get(userId);
        if (user != null) {
            return ServerResponse.ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(Mono.just(user), User.class);
        } else {
            return ServerResponse.notFound().build();
        }
    }

    public Mono<ServerResponse> createUser(ServerRequest request) {
        Mono<User> UserMono = request.bodyToMono(User.class);
        return UserMono.flatMap(User -> {
            userMap.put(User.getId(), User);
            return ServerResponse.created(request.uriBuilder().pathSegment(User.getId()).build())
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(Mono.just(User), User.class);
        });
    }


    public Mono<ServerResponse> delay(ServerRequest request) {

        log.info("hey, I'm doing something");
        String seconds = request.pathVariable("seconds");

        if (seconds != null) {
            log.info("{} thread", Thread.currentThread());
//            myService.doWork();
            return ServerResponse.ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(Mono.just(Thread.currentThread().toString()), String.class);
        } else {
            return ServerResponse.notFound().build();
        }
    }


}
