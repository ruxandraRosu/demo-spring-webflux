
http://localhost:8080/ui/docker-kafka-server/topic

Source of data: https://api.exchange.coinbase.com/products/ETH-USD/stats
https://docs.cloud.coinbase.com/exchange/reference

REST endpoints:
http://localhost:8090/api/products/ETH-USD/info
http://localhost:8070/api/products/ETH-USD/info
WS:
ws://localhost:8090/feed/trades
{
"channel":"",
"type": "trades",
"filters":{
"productId":["ETH-USD", "BTC-USD"]
}
}

https://blog.allegro.tech/2019/07/migrating-microservice-to-spring-webflux.html
