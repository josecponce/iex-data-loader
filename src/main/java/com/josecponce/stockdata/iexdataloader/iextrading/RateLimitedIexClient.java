package com.josecponce.stockdata.iexdataloader.iextrading;

import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import com.google.common.util.concurrent.RateLimiter;
import com.josecponce.stockdata.iexdataloader.batch.jpaentities.ExchangeSymbolEntity;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import pl.zankowski.iextrading4j.api.stocks.BatchStocks;
import pl.zankowski.iextrading4j.api.stocks.ChartRange;
import pl.zankowski.iextrading4j.client.IEXTradingClient;
import pl.zankowski.iextrading4j.client.rest.manager.RestRequest;
import pl.zankowski.iextrading4j.client.rest.request.stocks.BatchMarketStocksRequestBuilder;
import pl.zankowski.iextrading4j.client.rest.request.stocks.BatchStocksType;

import javax.ws.rs.ProcessingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

//429 = too mamy requests
@Service
@Slf4j
public class RateLimitedIexClient implements IexClient {
    private final RateLimiter rateLimiter = RateLimiter.create(80);
    private final IEXTradingClient client = IEXTradingClient.create();

    @Override
    public <R> R request(RestRequest<R> request) {
        rateLimiter.acquire();
        return client.executeRequest(request);
    }

    @Override
    public Map<String, BatchStocks> requestBatch(List<String> symbols, BatchStocksType type) {
        Assert.isTrue(symbols.size() <= 100, "Batch calls can be made for <=100 symbols at a time only");
        try {
            return request(getBatchRequest(symbols, type));
        } catch (ProcessingException e) {
            if (e.getCause() instanceof MismatchedInputException) {
                MismatchedInputException cause = (MismatchedInputException) e.getCause();
                val failedSymbol = cause.getPath().get(0);
                if (failedSymbol != null) {
                    val newSymbols = new ArrayList<>(symbols);
                    String failedSymbolString = failedSymbol.getFieldName();
                    newSymbols.remove(failedSymbolString);
                    log.error("Processing exception encountered for {}. Skipping.", failedSymbolString);
                    return request(getBatchRequest(newSymbols, type));
                }
            }
            throw e;
        }
    }

    private RestRequest<Map<String, BatchStocks>> getBatchRequest(List<String> symbols, BatchStocksType type) {
        return new BatchMarketStocksRequestBuilder()
                .withChartRange(ChartRange.FIVE_YEARS).addType(type).withSymbols(symbols).build();
    }
}
