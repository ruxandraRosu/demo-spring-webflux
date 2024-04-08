package com.techconnect.controller;

import com.techconnect.model.ProductInfo;
import com.techconnect.service.TradesService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@Slf4j
@AllArgsConstructor
@RequestMapping("api")
public class TradesController {
    private TradesService tradesService;


    @GetMapping("/products/{productId}/info")
    public Mono<ProductInfo> getProductStats(@PathVariable String productId) {
        log.info("{} thread", Thread.currentThread());
        return tradesService.getProductInfo(productId);
    }
}
