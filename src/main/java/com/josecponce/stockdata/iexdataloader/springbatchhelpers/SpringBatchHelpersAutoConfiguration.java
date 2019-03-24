package com.josecponce.stockdata.iexdataloader.springbatchhelpers;

import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import javax.persistence.EntityManagerFactory;

@Configuration
public class SpringBatchHelpersAutoConfiguration {

    @Bean
    public JobParametersBuilder jobParametersBuilder(JobExplorer jobExplorer) {
        return new JobParametersBuilder(jobExplorer);
    }

    @Bean
    public TaskExecutor executor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setDaemon(true);
        executor.setMaxPoolSize(130);
        executor.setCorePoolSize(50);
        executor.initialize();
        return executor;
    }

    @Bean
    public JpaItemWriter jpaWriter(EntityManagerFactory entityManagerFactory) {
        JpaItemWriter jpaWriter = new JpaItemWriter<>();
        jpaWriter.setEntityManagerFactory(entityManagerFactory);
        return jpaWriter;
    }
}
