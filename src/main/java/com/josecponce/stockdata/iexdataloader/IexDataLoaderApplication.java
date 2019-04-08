package com.josecponce.stockdata.iexdataloader;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

@SpringBootApplication
@EnableScheduling
public class IexDataLoaderApplication {

    public static void main(String[] args) {
        new SpringApplicationBuilder(IexDataLoaderApplication.class).web(WebApplicationType.NONE)
                .run(args);
    }

    @Bean
    public TaskScheduler scheduler() {
        ThreadPoolTaskScheduler threadPoolTaskScheduler = new ThreadPoolTaskScheduler();
        threadPoolTaskScheduler.setPoolSize(2);
        return threadPoolTaskScheduler;
    }
}
