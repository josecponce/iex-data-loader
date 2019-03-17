package com.josecponce.stockdata.iexdataloader.iextrading;

import com.google.common.util.concurrent.RateLimiter;
import com.josecponce.stockdata.iexdataloader.batch.jpaentities.DividendsEntity;
import com.josecponce.stockdata.iexdataloader.batch.jpaentities.ExchangeSymbolEntity;
import com.josecponce.stockdata.iexdataloader.batch.jpaentities.KeyStatsEntity;
import com.josecponce.stockdata.iexdataloader.batch.jpaentities.SplitEntity;
import lombok.extern.slf4j.Slf4j;
import org.reactivestreams.Publisher;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

//429 = too mamy requests
@Service
@Slf4j
public class RateLimitedIexClient implements IexClient {
    private final RateLimiter rateLimiter = RateLimiter.create(100);
    private final WebClient client;

    public RateLimitedIexClient(WebClient client) {
        this.client = client;
    }

    @Override
    public Flux<ExchangeSymbolEntity> getSymbols() {
        return client.get().uri("/ref-data/symbols")
                .exchange()
                .doOnError(t -> log.error("Failed to retrieve list of exchange symbols.", t))
                .flatMapMany(clientResponse -> clientResponse.bodyToFlux(ExchangeSymbolEntity.class))
                .doOnSubscribe(subscription -> rateLimiter.acquire());
    }

    @Override
    public Flux<DividendsEntity> getDividends(String symbol) {
        return client.get().uri("/stock/{symbol}/dividends/5y", symbol)
                .exchange()
                .doOnError(t -> log.error(String.format("Failed to retrieve dividends for symbol '%s'", symbol), t))
                .flatMapMany(clientResponse -> clientResponse.bodyToFlux(DividendsEntity.class))
                .doOnNext(dividendsEntity -> dividendsEntity.setSymbol(symbol))
                .doOnSubscribe(subscription -> rateLimiter.acquire());
    }

    @Override
    public Flux<SplitEntity> getSplits(String symbol) {
        return client.get().uri("/stock/{symbol}/splits/5y", symbol)
                .exchange()
                .doOnError(t -> log.error(String.format("Failed to retrieve splits for symbol '%s'", symbol), t))
                .flatMapMany(clientResponse -> clientResponse.bodyToFlux(SplitEntity.class))
                .doOnNext(splitEntity -> splitEntity.setSymbol(symbol))
                .doOnSubscribe(subscription -> rateLimiter.acquire());
    }

    @Override
    public Mono<KeyStatsEntity> getKeyStats(String symbol) {
        return client.get().uri("/stock/{symbol}/stats", symbol)
                .exchange()
                .doOnError(t -> log.error(String.format("Failed to retrieve key stats for symbol '%s'", symbol), t))
                .flatMap(clientResponse -> clientResponse.bodyToMono(KeyStatsEntity.class))
                .doOnSubscribe(subscription -> rateLimiter.acquire());
    }
}
