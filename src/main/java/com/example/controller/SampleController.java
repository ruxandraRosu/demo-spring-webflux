package com.example.controller;

import com.example.service.MyService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

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
}
