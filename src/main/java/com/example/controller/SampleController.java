package com.example.controller;

import com.example.dto.MyDto;
import com.example.service.MyService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@Slf4j
@AllArgsConstructor
public class SampleController {

    private MyService myService;

    @GetMapping("/block/{seconds}")
    public String delay(@PathVariable int seconds) {
        myService.callExternalService(seconds);
        log.info("{} thread", Thread.currentThread());
        return Thread.currentThread().toString();
    }

    @PostMapping ("/messages")
    public Mono sendMessage(@RequestBody MyDto request) {
        myService.publishMessage(request);
        log.info("{} thread", Thread.currentThread());
        return Mono.just(Thread.currentThread().toString());
    }
    @PostMapping ("/signatures")
    public Mono doWork(@RequestBody List<MyDto> list) {
        myService.doWork(list);
        log.info("{} thread", Thread.currentThread());
        return Mono.just(Thread.currentThread().toString());
    }
}
