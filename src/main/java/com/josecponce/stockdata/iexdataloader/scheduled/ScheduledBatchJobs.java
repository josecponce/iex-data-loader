package com.josecponce.stockdata.iexdataloader.scheduled;

import com.josecponce.stockdata.iexdataloader.batch.jpaentities.ExchangeSymbolEntity;
import com.josecponce.stockdata.iexdataloader.batch.repositories.DividendsRepository;
import com.josecponce.stockdata.iexdataloader.batch.repositories.ExchangeSymbolRepository;
import com.josecponce.stockdata.iexdataloader.batch.repositories.KeyStatsRepository;
import com.josecponce.stockdata.iexdataloader.batch.repositories.SplitRepository;
import com.josecponce.stockdata.iexdataloader.iextrading.IexClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import reactor.core.Disposable;
import reactor.core.publisher.Flux;

@Component
@Slf4j
public class ScheduledBatchJobs {
    private final IexClient client;
    private final ExchangeSymbolRepository symbolRepository;
    private final KeyStatsRepository keyStatsRepository;
    private final SplitRepository splitRepository;
    private final DividendsRepository dividendsRepository;

    public ScheduledBatchJobs(IexClient client, ExchangeSymbolRepository symbolRepository, KeyStatsRepository keyStatsRepository, SplitRepository splitRepository, DividendsRepository dividendsRepository) {
        this.client = client;
        this.symbolRepository = symbolRepository;
        this.keyStatsRepository = keyStatsRepository;
        this.splitRepository = splitRepository;
        this.dividendsRepository = dividendsRepository;
    }

    @Scheduled(fixedDelayString = "${fixedDelayMs}")
    public void run() {
        Flux<ExchangeSymbolEntity> symbols = client.getSymbols()
                .doOnNext(symbolRepository::save)
                .doOnError(t -> log.error("Failed to save exchange symbol to the database."));

        symbols
                .flatMap(symbol -> client.getKeyStats(symbol.getSymbol()))
                .doOnNext(keyStatsRepository::save)
                .doOnError(t -> log.error("Failed to save key stats to the database."))
                .blockLast();
//        Disposable dividends = symbols
//                .flatMap(symbol -> client.getDividends(symbol.getSymbol()))
//                .doOnNext(dividendsRepository::save)
//                .doOnError(t -> log.error("Failed to save dividends to the database."))
//                .subscribe();
//        Disposable splits = symbols
//                .flatMap(symbol -> client.getSplits(symbol.getSymbol()))
//                .doOnNext(splitRepository::save)
//                .doOnError(t -> log.error("Failed to save splits to the database."))
//                .subscribe();
    }
}
