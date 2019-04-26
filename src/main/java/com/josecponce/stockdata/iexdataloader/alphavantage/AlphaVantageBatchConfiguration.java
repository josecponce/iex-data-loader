package com.josecponce.stockdata.iexdataloader.alphavantage;

import com.josecponce.stockdata.iexdataloader.alphavantage.api.AlphaVantageClient;
import com.josecponce.stockdata.iexdataloader.alphavantage.api.OutputSize;
import com.josecponce.stockdata.iexdataloader.alphavantage.api.TimeSeriesDailyAdjustedDTO;
import com.josecponce.stockdata.iexdataloader.alphavantage.jpaentities.TimeSeriesDailyAdjustedEntity;
import com.josecponce.stockdata.iexdataloader.alphavantage.repositories.TimeSeriesDailyAdjustedRepository;
import com.josecponce.stockdata.iexdataloader.management.jpaentities.AlphaVantageLoadStockEntity;
import com.josecponce.stockdata.iexdataloader.springbatchhelpers.stepbuilder.ParallelJpaToJpaStepBuilder;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import java.util.List;
import java.util.stream.Collectors;

@Configuration
@Slf4j
public class AlphaVantageBatchConfiguration {
    public static final String LOAD_ALPHA_VANTAGE_STOCK_DATA_JOB = "Load Alpha Vantage Stock Data";
    private static final String LOAD_ALPHA_VANTAGE_STOCK_PRICE_DATA_STEP = "Load Alpha Vantage Stock Price Data";

    private final JobBuilderFactory jobBuilder;
    private final AlphaVantageClient client;
    private final TimeSeriesDailyAdjustedRepository repository;

    public AlphaVantageBatchConfiguration(JobBuilderFactory jobBuilder, AlphaVantageClient client, TimeSeriesDailyAdjustedRepository repository) {
        this.jobBuilder = jobBuilder;
        this.client = client;
        this.repository = repository;
    }

    @Bean(LOAD_ALPHA_VANTAGE_STOCK_DATA_JOB)
    @Scope(scopeName = "prototype")
    public Job job(@Qualifier(LOAD_ALPHA_VANTAGE_STOCK_PRICE_DATA_STEP) Step loadStockPrices) {
        return jobBuilder.get(LOAD_ALPHA_VANTAGE_STOCK_DATA_JOB)
                .preventRestart()
                .incrementer(new RunIdIncrementer())
                .start(loadStockPrices)
                .build();
    }

    @Bean(LOAD_ALPHA_VANTAGE_STOCK_PRICE_DATA_STEP)
    @Scope(scopeName = "prototype")
    public Step loadStockPrice(ParallelJpaToJpaStepBuilder<AlphaVantageLoadStockEntity, List<TimeSeriesDailyAdjustedEntity>> stepBuilder) {
        return stepBuilder.withName(LOAD_ALPHA_VANTAGE_STOCK_PRICE_DATA_STEP)
                .withChunk(1)
                .withConcurrency(3)
                .withProcessor(new ItemProcessor<AlphaVantageLoadStockEntity, List<TimeSeriesDailyAdjustedEntity>>() {
                    @Override
                    public List<TimeSeriesDailyAdjustedEntity> process(AlphaVantageLoadStockEntity item) {
                        TimeSeriesDailyAdjustedDTO response = null;
                        try {
                            response = client.requestTimeSeriesDailyAdjusted(item.getSymbol(), OutputSize.full);
                            List<TimeSeriesDailyAdjustedEntity> results = response.toEntities()
                                    .filter(price -> {
                                        val current = repository.findById(new TimeSeriesDailyAdjustedEntity.TimeSeriesDailyAdjustedId
                                                (item.getSymbol(), price.getDate()));
                                        return !current.isPresent() || !current.get().equals(price);
                                    }).collect(Collectors.toList());
                            return results.isEmpty() ? null : results;
                        } catch (NullPointerException e) {
                            log.error("Found the null pointer exception", e);
                            if (response != null) {
                                log.error("This is the response from AlphaVantage that caused the null pointer exception: {}", response);
                            }
                            throw e;
                        }
                    }
                })
                .build();

    }

}
