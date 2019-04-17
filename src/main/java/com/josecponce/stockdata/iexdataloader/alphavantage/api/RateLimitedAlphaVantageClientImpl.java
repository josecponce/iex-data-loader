package com.josecponce.stockdata.iexdataloader.alphavantage.api;

import com.google.common.util.concurrent.RateLimiter;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Service
@Slf4j
public class RateLimitedAlphaVantageClientImpl implements AlphaVantageClient {
    private final String ROOT = "https://www.alphavantage.co";
    private final RateLimiter rateLimiter = RateLimiter.create(0.07); //approx 4.2 requests/minute
    private final RestTemplate restTemplate;
    private final String apiKey;

    public RateLimitedAlphaVantageClientImpl(RestTemplateBuilder restTemplateBuilder, @Value("${alphaVantageApiKey}") String apiKey) {
        this.restTemplate = restTemplateBuilder.rootUri(ROOT).build();
        this.apiKey = apiKey;
    }

    @Override
    public TimeSeriesDailyAdjustedDTO requestTimeSeriesDailyAdjusted(String symbol, OutputSize size) {
        try {
            rateLimiter.acquire();
            val url = UriComponentsBuilder.fromUriString("/query")
                    .queryParam("function", "TIME_SERIES_DAILY_ADJUSTED")
                    .queryParam("symbol", symbol)
                    .queryParam("outputsize", size.toString())
                    .queryParam("apikey", apiKey)
                    .build().toUriString();
            return restTemplate.getForObject(url, TimeSeriesDailyAdjustedDTO.class);
        } catch (HttpStatusCodeException e) {
            log.error("Failed to retrieve daily adjusted time series data from alpha vantage api for '{}' from with status code '{}'", symbol, e.getStatusCode());
            throw e;
        }
    }
}
