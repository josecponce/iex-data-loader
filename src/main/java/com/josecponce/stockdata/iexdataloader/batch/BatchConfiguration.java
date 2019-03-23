package com.josecponce.stockdata.iexdataloader.batch;

import com.josecponce.stockdata.iexdataloader.batch.jpaentities.*;
import com.josecponce.stockdata.iexdataloader.iextrading.IexClient;
import josecponce.springbatchhelpers.readers.SynchronizedIteratorItemReader;
import josecponce.springbatchhelpers.stepbuilder.ParallelJpaToJpaStepBuilder;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import lombok.var;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.job.builder.FlowBuilder;
import org.springframework.batch.core.job.flow.Flow;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.core.task.TaskExecutor;
import pl.zankowski.iextrading4j.api.refdata.ExchangeSymbol;
import pl.zankowski.iextrading4j.api.stocks.BatchStocks;
import pl.zankowski.iextrading4j.api.stocks.ChartRange;
import pl.zankowski.iextrading4j.api.stocks.Dividends;
import pl.zankowski.iextrading4j.api.stocks.KeyStats;
import pl.zankowski.iextrading4j.client.rest.manager.RestRequest;
import pl.zankowski.iextrading4j.client.rest.request.refdata.SymbolsRequestBuilder;
import pl.zankowski.iextrading4j.client.rest.request.stocks.*;

import javax.ws.rs.ProcessingException;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;


@Configuration
@EnableBatchProcessing
@Slf4j
public class BatchConfiguration {
    private static final String LOAD_SYMBOL_DEPENDENT_DATA_FLOW = "Load Symbol Dependent Data";
    private static final String LOAD_IEX_DATA_JOB = "Load IEX data";
    private static final String LOAD_EXCHANGE_SYMBOLS_STEP = "Load Exchange Symbols";
    private static final String LOAD_KEY_STATS_STEP = "Load Key Stats";
    private static final String LOAD_DIVIDENDS_STEP = "Load Dividends";
    private static final String LOAD_SPLITS_STEP = "Load Splits";

    private final JobBuilderFactory jobBuilder;
    private final IexClient client;
    private final ApiEntityConverter converter;
    private final TaskExecutor executor;

    public BatchConfiguration(JobBuilderFactory jobBuilder, IexClient client, ApiEntityConverter converter, TaskExecutor executor) {
        this.jobBuilder = jobBuilder;
        this.client = client;
        this.converter = converter;
        this.executor = executor;
    }

    @Bean
    @Scope(scopeName = "prototype")
    public Job job(@Qualifier(LOAD_EXCHANGE_SYMBOLS_STEP) Step exchangeSymbols,
                   @Qualifier(LOAD_KEY_STATS_STEP) Step keyStats,
                   @Qualifier(LOAD_DIVIDENDS_STEP) Step dividends,
                   @Qualifier(LOAD_SPLITS_STEP) Step splits) {
        return jobBuilder.get(LOAD_IEX_DATA_JOB)
                .preventRestart()
                .incrementer(new RunIdIncrementer())
                .start(new FlowBuilder<Flow>(LOAD_EXCHANGE_SYMBOLS_STEP).start(exchangeSymbols).end())
                .next(new FlowBuilder<Flow>(LOAD_SYMBOL_DEPENDENT_DATA_FLOW)
                        .split(executor).add(
                                new FlowBuilder<Flow>(LOAD_KEY_STATS_STEP).start(keyStats).end(),
                                new FlowBuilder<Flow>(LOAD_DIVIDENDS_STEP).start(dividends).end(),
                                new FlowBuilder<Flow>(LOAD_SPLITS_STEP).start(splits).end()
                        ).end())
                .end()
                .build();
    }

    @Bean(LOAD_EXCHANGE_SYMBOLS_STEP)
    @Scope(scopeName = "prototype")
    public Step loadExchangeSymbols(ParallelJpaToJpaStepBuilder<ExchangeSymbol, ExchangeSymbolEntity> stepBuilder) {
        return stepBuilder.withName(LOAD_EXCHANGE_SYMBOLS_STEP)
                .withChunk(100)
                .withConcurrency(5)
                .withInClass(ExchangeSymbol.class)
                .withReader(new SynchronizedIteratorItemReader<>(client.request(new SymbolsRequestBuilder().build()).iterator()))
                .withProcessor(symbol -> converter.convert(symbol, ExchangeSymbolEntity.class))
                .withOutClass(ExchangeSymbolEntity.class)
                .build();
    }

    @Bean(LOAD_KEY_STATS_STEP)
    @Scope(scopeName = "prototype")
    public Step loadKeyStats(ParallelJpaToJpaStepBuilder<List<ExchangeSymbolEntity>, List<KeyStatsEntity>> stepBuilder) {
        return stepBuilder.withName(LOAD_KEY_STATS_STEP)
                .withChunk(100)
                .withConcurrency(10)
                .withInComponentClass(ExchangeSymbolEntity.class)
                .withProcessor(new ItemProcessor<List<ExchangeSymbolEntity>, List<KeyStatsEntity>>() {
                    @Override
                    public List<KeyStatsEntity> process(List<ExchangeSymbolEntity> symbols) throws Exception {
                        val stats = client.requestBatch(symbols.stream().map(ExchangeSymbolEntity::getSymbolEncoded).collect(Collectors.toList()),
                                BatchStocksType.KEY_STATS);
                        return stats.entrySet().stream().map(kv -> {
                            KeyStatsEntity stat = converter.convert(kv.getValue().getKeyStats(), KeyStatsEntity.class);
                            stat.setSymbol(kv.getKey());
                            return stat;
                        }).collect(Collectors.toList());//apparently there are some symbols that don't have stats
                    }
                })
                .build();
    }

    @Bean(LOAD_DIVIDENDS_STEP)
    @Scope(scopeName = "prototype")
    public Step loadDividends(ParallelJpaToJpaStepBuilder<ExchangeSymbolEntity, List<DividendsEntity>> stepBuilder) {
        return stepBuilder.withName(LOAD_DIVIDENDS_STEP)
                .withChunk(100)
                .withConcurrency(8)
                .withProcessor(new ItemProcessor<ExchangeSymbolEntity, List<DividendsEntity>>() {
                    @Override
                    public List<DividendsEntity> process(ExchangeSymbolEntity symbol) throws Exception {
                        val dividends = client.request(new DividendsRequestBuilder().withSymbol(symbol.getSymbolEncoded())
                                .withDividendRange(DividendRange.FIVE_YEARS).build());
                        return dividends.stream().map(div -> converter.convert(div, DividendsEntity.class))
                                .peek(div -> div.setSymbol(symbol.getSymbol())).collect(Collectors.toList());
                    }
                })
                .build();
    }

    @Bean(LOAD_SPLITS_STEP)
    @Scope(scopeName = "prototype")
    public Step loadSplits(ParallelJpaToJpaStepBuilder<ExchangeSymbolEntity, List<SplitEntity>> stepBuilder) {
        return stepBuilder.withName(LOAD_SPLITS_STEP)
                .withChunk(100)
                .withConcurrency(3)
                .withSkip(Collections.singletonList(ProcessingException.class))
                .withProcessor(new ItemProcessor<ExchangeSymbolEntity, List<SplitEntity>>() {
                    @Override
                    public List<SplitEntity> process(ExchangeSymbolEntity symbol) throws Exception {
                        val splits = client.request(new SplitsRequestBuilder().withSymbol(symbol.getSymbolEncoded())
                                .withSplitsRange(SplitsRange.FIVE_YEARS).build());
                        return splits.stream().map(split -> converter.convert(split, SplitEntity.class))
                                .peek(split -> split.setSymbol(symbol.getSymbol())).collect(Collectors.toList());
                    }
                })
                .build();
    }
}
