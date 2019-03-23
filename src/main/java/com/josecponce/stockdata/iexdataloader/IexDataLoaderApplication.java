package com.josecponce.stockdata.iexdataloader;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class IexDataLoaderApplication {

    public static void main(String[] args) {
        SpringApplication.run(IexDataLoaderApplication.class, args);
    }
}
