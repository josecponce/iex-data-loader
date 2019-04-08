package com.josecponce.stockdata.iexdataloader.gov;

import com.josecponce.stockdata.iexdataloader.gov.dataapi.TreasuryDataClient;
import com.josecponce.stockdata.iexdataloader.gov.jpaentities.TreasuryYieldEntity;
import com.josecponce.stockdata.iexdataloader.springbatchhelpers.readers.SynchronizedIteratorItemReader;
import com.josecponce.stockdata.iexdataloader.springbatchhelpers.stepbuilder.ParallelJpaToJpaStepBuilder;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

@Configuration
@EnableBatchProcessing
@Slf4j
public class GovBatchConfiguration {

    public static final String LOAD_TREASURY_DATA_JOB = "Load Treasury Data";
    private static final String LOAD_TREASURY_YIELD_DATA_STEP = "Load Treasury Yield Data";
    private final JobBuilderFactory jobBuilder;
    private final TreasuryDataClient treasuryDataClient;
    private Integer daysBack;

    public GovBatchConfiguration(JobBuilderFactory jobBuilder, TreasuryDataClient treasuryDataClient, @Value("${treasuryRangeDays}") Integer daysBack) {
        this.jobBuilder = jobBuilder;
        this.treasuryDataClient = treasuryDataClient;
        this.daysBack = daysBack;
    }

    @Bean(LOAD_TREASURY_DATA_JOB)
    @Scope(scopeName = "prototype")
    public Job job(@Qualifier(LOAD_TREASURY_YIELD_DATA_STEP) Step yields) {
        return jobBuilder.get(LOAD_TREASURY_DATA_JOB)
                .preventRestart()
                .incrementer(new RunIdIncrementer())
                .start(yields)
                .build();
    }

    @Bean(LOAD_TREASURY_YIELD_DATA_STEP)
    @Scope(scopeName = "prototype")
    public Step loadTreasuryYields(ParallelJpaToJpaStepBuilder<TreasuryYieldEntity, TreasuryYieldEntity> stepBuilder) {
        return stepBuilder.withName(LOAD_TREASURY_YIELD_DATA_STEP)
                .withChunk(500)
                .withConcurrency(3)
                .withInClass(TreasuryYieldEntity.class)
                .withReader(new SynchronizedIteratorItemReader<>(treasuryDataClient.requestLastNDays(getDaysBack()).iterator()))
                .withOutClass(TreasuryYieldEntity.class)
                .build();
    }

    private Integer getDaysBack() {
        val temp = daysBack;
        daysBack = 30;
        return temp;
    }
}
