package com.josecponce.stockdata.iexdataloader.iex;

import com.josecponce.stockdata.iexdataloader.iex.iextrading.IexClient;
import com.josecponce.stockdata.iexdataloader.iex.jpaentities.*;
import com.josecponce.stockdata.iexdataloader.iex.repositories.*;
import com.josecponce.stockdata.iexdataloader.springbatchhelpers.readers.SynchronizedIteratorItemReader;
import com.josecponce.stockdata.iexdataloader.springbatchhelpers.stepbuilder.ParallelJpaToJpaStepBuilder;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
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
import pl.zankowski.iextrading4j.client.rest.request.refdata.SymbolsRequestBuilder;
import pl.zankowski.iextrading4j.client.rest.request.stocks.BatchStocksType;

import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;


@Configuration
@EnableBatchProcessing
@Slf4j
public class IexBatchConfiguration {
    public static final String LOAD_IEX_DATA_JOB = "Load IEX data";
    private static final String LOAD_SYMBOL_DEPENDENT_DATA_FLOW = "Load Symbol Dependent Data";
    private static final String LOAD_EXCHANGE_SYMBOLS_STEP = "Load Exchange Symbols";
    private static final String LOAD_KEY_STATS_STEP = "Load Key Stats";
    private static final String LOAD_DIVIDENDS_STEP = "Load Dividends";
    private static final String LOAD_SPLITS_STEP = "Load Splits";
    private static final String LOAD_CHART_DATA_STEP = "Load Chart data";
    private static final String ADJUST_CHART_PRICES_FOR_SPLITS_STEP = "Adjust Chart Prices For Splits";

    private final JobBuilderFactory jobBuilder;
    private final IexClient client;
    private final ApiEntityConverter converter;
    private final TaskExecutor executor;

    private final SplitRepository splitRepository;
    private final ExchangeSymbolRepository symbolRepository;
    private final KeyStatsRepository keyStatsRepository;
    private final DividendsRepository dividendsRepository;
    private final ChartRepository chartRepository;

    private LocalDateTime jobStartTime;

    public IexBatchConfiguration(JobBuilderFactory jobBuilder, IexClient client, ApiEntityConverter converter, TaskExecutor executor, SplitRepository splitRepository, ExchangeSymbolRepository symbolRepository, KeyStatsRepository keyStatsRepository, DividendsRepository dividendsRepository, ChartRepository chartRepository) {
        this.jobBuilder = jobBuilder;
        this.client = client;
        this.converter = converter;
        this.executor = executor;
        this.splitRepository = splitRepository;
        this.symbolRepository = symbolRepository;
        this.keyStatsRepository = keyStatsRepository;
        this.dividendsRepository = dividendsRepository;
        this.chartRepository = chartRepository;
    }

    @Bean(LOAD_IEX_DATA_JOB)
    @Scope(scopeName = "prototype")
    public Job job(@Qualifier(LOAD_EXCHANGE_SYMBOLS_STEP) Step exchangeSymbols,
                   @Qualifier(LOAD_KEY_STATS_STEP) Step keyStats,
                   @Qualifier(LOAD_DIVIDENDS_STEP) Step dividends,
                   @Qualifier(LOAD_SPLITS_STEP) Step splits,
                   @Qualifier(LOAD_CHART_DATA_STEP) Step charts,
                   @Qualifier(ADJUST_CHART_PRICES_FOR_SPLITS_STEP) Step adjustCharts) {
        return jobBuilder.get(LOAD_IEX_DATA_JOB)
                .preventRestart()
                .incrementer(new RunIdIncrementer())
                .listener(new JobExecutionListener() {
                    @Override
                    public void beforeJob(JobExecution jobExecution) {
                        jobStartTime = LocalDateTime.now();
                    }

                    @Override
                    public void afterJob(JobExecution jobExecution) {
                        jobStartTime = null;
                    }
                })
                .start(new FlowBuilder<Flow>(LOAD_EXCHANGE_SYMBOLS_STEP).start(exchangeSymbols).end())
                .next(new FlowBuilder<Flow>(LOAD_SYMBOL_DEPENDENT_DATA_FLOW)
                        .split(executor).add(
                                new FlowBuilder<Flow>(LOAD_KEY_STATS_STEP).start(keyStats).end()
                                ,new FlowBuilder<Flow>(LOAD_DIVIDENDS_STEP).start(dividends).end()
                                ,new FlowBuilder<Flow>(LOAD_SPLITS_STEP).start(splits).end()
                                ,new FlowBuilder<Flow>(LOAD_CHART_DATA_STEP).start(charts).end()
                        ).end())
                .next(new FlowBuilder<Flow>(ADJUST_CHART_PRICES_FOR_SPLITS_STEP).start(adjustCharts).end())
                .end()
                .build();
    }

    @Bean(LOAD_EXCHANGE_SYMBOLS_STEP)
    @Scope(scopeName = "prototype")
    public Step loadExchangeSymbols(ParallelJpaToJpaStepBuilder<ExchangeSymbol, ExchangeSymbolEntity> stepBuilder) {
        Iterator<ExchangeSymbol> symbolIterator = client.request(new SymbolsRequestBuilder().build()).iterator();
        return stepBuilder.withName(LOAD_EXCHANGE_SYMBOLS_STEP)
                .withChunk(100)
                .withConcurrency(20)
                .withInClass(ExchangeSymbol.class)
                .withReader(new SynchronizedIteratorItemReader<>(symbolIterator))
                .withProcessor(symbol -> {
                    val current = symbolRepository.findById(symbol.getSymbol());
                    val result = converter.convert(symbol, ExchangeSymbolEntity.class);
                    return current.isPresent() && current.get().equals(result) ? null : result;
                })
                .withOutClass(ExchangeSymbolEntity.class)
                .build();
    }

    @Bean(LOAD_KEY_STATS_STEP)
    @Scope(scopeName = "prototype")
    public Step loadKeyStats(ParallelJpaToJpaStepBuilder<List<ExchangeSymbolEntity>, List<KeyStatsEntity>> stepBuilder) {
        return stepBuilder.withName(LOAD_KEY_STATS_STEP)
                .withChunk(100)
                .withConcurrency(5)
                .withInComponentClass(ExchangeSymbolEntity.class)
                .withProcessor(new ItemProcessor<List<ExchangeSymbolEntity>, List<KeyStatsEntity>>() {
                    @Override
                    public List<KeyStatsEntity> process(List<ExchangeSymbolEntity> symbols) {
                        val stats = client.requestBatch(getSymbols(symbols), BatchStocksType.KEY_STATS);
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
    public Step loadDividends(ParallelJpaToJpaStepBuilder<List<ExchangeSymbolEntity>, List<DividendsEntity>> stepBuilder) {
        return stepBuilder.withName(LOAD_DIVIDENDS_STEP)
                .withChunk(100)
                .withConcurrency(5)
                .withInComponentClass(ExchangeSymbolEntity.class)
                .withProcessor(new ItemProcessor<List<ExchangeSymbolEntity>, List<DividendsEntity>>() {
                    @Override
                    public List<DividendsEntity> process(List<ExchangeSymbolEntity> symbols) {
                        val dividends = client.requestBatch(getSymbols(symbols), BatchStocksType.DIVIDENDS);
                        return dividends.entrySet().stream().flatMap(kv ->
                                kv.getValue().getDividends().stream().map(div -> {
                                    val result = converter.convert(div, DividendsEntity.class);
                                    result.setSymbol(kv.getKey());
                                    val current = dividendsRepository.findById(
                                            new DividendsEntity.DividendsEntityId(kv.getKey(), div.getExDate()));
                                    return current.isPresent() && current.get().equals(result) ? null : result;
                                }).filter(Objects::nonNull)).collect(Collectors.toList());
                    }
                })
                .build();
    }

    @Bean(LOAD_SPLITS_STEP)
    @Scope(scopeName = "prototype")
    public Step loadSplits(ParallelJpaToJpaStepBuilder<List<ExchangeSymbolEntity>, List<SplitEntity>> stepBuilder) {
        return stepBuilder.withName(LOAD_SPLITS_STEP)
                .withChunk(100)
                .withConcurrency(1)
                .withInComponentClass(ExchangeSymbolEntity.class)
                .withProcessor(new ItemProcessor<List<ExchangeSymbolEntity>, List<SplitEntity>>() {
                    @Override
                    public List<SplitEntity> process(List<ExchangeSymbolEntity> symbols) {
                        Map<String, BatchStocks> splits = client.requestBatch(getSymbols(symbols), BatchStocksType.SPLITS);
                        return splits.entrySet().stream().flatMap(kv ->
                                kv.getValue().getSplits().stream().map(split -> {
                                    val result = converter.convert(split, SplitEntity.class);
                                    result.setSymbol(kv.getKey());
                                    val current = splitRepository.findById(
                                            new SplitEntity.SplitEntityId(kv.getKey(), result.getExDate()));
                                    return current.isPresent() && current.get().equals(result) ? null : result;
                                }).filter(Objects::nonNull)).collect(Collectors.toList());
                    }
                })
                .build();
    }

    @Bean(LOAD_CHART_DATA_STEP)
    @Scope(scopeName = "prototype")
    public Step loadChartData(ParallelJpaToJpaStepBuilder<List<ExchangeSymbolEntity>, List<ChartEntity>> stepBuilder) {
        return stepBuilder.withName(LOAD_CHART_DATA_STEP)
                .withChunk(5)
                .withConcurrency(20)
                .withInComponentClass(ExchangeSymbolEntity.class)
                .withProcessor(new ItemProcessor<List<ExchangeSymbolEntity>, List<ChartEntity>>() {
                    @Override
                    public List<ChartEntity> process(List<ExchangeSymbolEntity> symbols) {
                        return client.requestBatch(getSymbols(symbols), BatchStocksType.CHART)
                                .entrySet().stream().flatMap(kv ->
                                        kv.getValue().getChart().stream().map(c -> {
                                            val result = converter.convert(c, ChartEntity.class);
                                            result.setSymbol(kv.getKey());
                                            result.setDateDate(LocalDate.parse(c.getDate()));
                                            val current = chartRepository.findById(
                                                    new ChartEntity.ChartEntityId(kv.getKey(), result.getDate()));
                                            return current.isPresent() && current.get().equals(result) ? null : result;
                                        }).filter(Objects::nonNull)).collect(Collectors.toList());
                    }
                })
                .build();
    }

    @Bean(ADJUST_CHART_PRICES_FOR_SPLITS_STEP)
    @Scope(scopeName = "prototype")
    public Step adjustChartPricesForSplits(ParallelJpaToJpaStepBuilder<ChartEntity, ChartEntity> stepBuilder) {
        return stepBuilder.withName(ADJUST_CHART_PRICES_FOR_SPLITS_STEP)
                .withChunk(1000)
                .withConcurrency(50)//depending on sort order so it has to be single threaded
                .withOrderBy("symbol")
                .withProcessor(new ItemProcessor<ChartEntity, ChartEntity>() {
                    private ThreadLocal<String> splitsSymbol = new ThreadLocal<>();
                    private ThreadLocal<List<SplitEntity>> splits = new ThreadLocal<>();

                    @Override
                    public ChartEntity process(ChartEntity item) {
                        if (item.getLastUpdated().isAfter(jobStartTime)) {
                            return null;//no need to update
                        }
                        if (splitsSymbol.get() == null || !splitsSymbol.get().equals(item.getSymbol())) {
                            splits.set(splitRepository.findAllBySymbolAndLastUpdatedAfter(item.getSymbol(), jobStartTime));
                            splitsSymbol.set(item.getSymbol());
                        }
                        if (splits.get().isEmpty()) {
                            return null;//no splits to apply
                        }
                        for (val split : splits.get()) {
                            item.setClose(item.getClose().multiply(split.getToFactor()).divide(split.getForFactor(), RoundingMode.HALF_UP));
                        }
                        return item;
                    }
                })
                .build();
    }

    private List<String> getSymbols(List<ExchangeSymbolEntity> symbols) {
        return symbols.stream().map(ExchangeSymbolEntity::getSymbolEncoded).collect(Collectors.toList());
    }
}
