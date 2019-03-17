package com.josecponce.stockdata.iexdataloader;

import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.netty.channel.ChannelOption;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.text.SimpleDateFormat;

@SpringBootApplication
@EnableScheduling
public class IexDataLoaderApplication {

    public static void main(String[] args) {
        SpringApplication.run(IexDataLoaderApplication.class, args);
    }

    @Bean
    public Module dateTimeModule() {
        return new JavaTimeModule();
    }

    @Bean
    public WebClient webClient(ObjectMapper mapper) {
        mapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S"));
        return WebClient.builder()
                .baseUrl("https://api.iextrading.com/1.0/")
                .clientConnector(new ReactorClientHttpConnector(builder -> builder.option(ChannelOption.SO_TIMEOUT, 100)))
                .build();
    }
}
