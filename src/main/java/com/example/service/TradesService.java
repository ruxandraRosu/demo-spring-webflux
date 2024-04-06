package com.example.service;

import com.example.model.Product;
import com.example.model.ProductInfo;
import com.example.model.Stats;
import com.example.model.response.Trade;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.Disposable;
import reactor.core.publisher.Mono;

@Service
@AllArgsConstructor
@Slf4j
public class TradesService {

    private final WebClient webClient;
    private final KafkaService kafkaService;
    private ObjectMapper mapper;

    public Mono<Product> getProduct(String productId) {
        log.info("Before rest call {} for tradeId {}", Thread.currentThread(), productId);
        return webClient.get()
                .uri("/products/" + productId)
                .retrieve()
                .bodyToMono(Product.class)
                .onErrorMap(RuntimeException::new);
    }

    public Mono<Stats> getProductStats(String productId) {
        log.info("Before rest call {} for productId {}", Thread.currentThread(), productId);
        return webClient.get()
                .uri("/products/" + productId + "/stats")
                .retrieve()
                .bodyToMono(Stats.class)
                .onErrorMap(RuntimeException::new);
    }

    public Mono<Trade> decorateTrade(Trade trade) {
        Mono<Trade> tradeMono = Mono.just(trade);
        Mono<Product> productMono = getProduct(trade.getGridKey());
        return Mono.just(Trade.builder())
                .zipWith(tradeMono, Trade.TradeBuilder::trade)
                .zipWith(productMono, Trade.TradeBuilder::product)
                .map(Trade.TradeBuilder::build);
    }

    public Disposable publishMessage(Mono<Trade> trade) {
        return kafkaService.sendMessage(trade)
                .subscribe();
    }


    public Mono<ProductInfo> getProductInfo(String productId) {
        Mono<Stats> stats = getProductStats(productId);
        Mono<Product> product = getProduct(productId);

        return Mono.just(ProductInfo.builder())
                .zipWith(product, ProductInfo.ProductInfoBuilder::product)
                .zipWith(stats, ProductInfo.ProductInfoBuilder::stats)
                .map(ProductInfo.ProductInfoBuilder::build);


    }
}
