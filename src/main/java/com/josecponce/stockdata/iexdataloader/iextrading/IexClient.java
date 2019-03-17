package com.josecponce.stockdata.iexdataloader.iextrading;

import com.josecponce.stockdata.iexdataloader.batch.jpaentities.DividendsEntity;
import com.josecponce.stockdata.iexdataloader.batch.jpaentities.ExchangeSymbolEntity;
import com.josecponce.stockdata.iexdataloader.batch.jpaentities.KeyStatsEntity;
import com.josecponce.stockdata.iexdataloader.batch.jpaentities.SplitEntity;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface IexClient {
    Flux<ExchangeSymbolEntity> getSymbols();
    Flux<DividendsEntity> getDividends(String symbol);
    Flux<SplitEntity> getSplits(String symbol);
    Mono<KeyStatsEntity> getKeyStats(String symbol);
}
