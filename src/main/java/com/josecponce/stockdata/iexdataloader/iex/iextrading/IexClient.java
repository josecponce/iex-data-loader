package com.josecponce.stockdata.iexdataloader.iex.iextrading;

import pl.zankowski.iextrading4j.api.stocks.BatchStocks;
import pl.zankowski.iextrading4j.api.stocks.ChartRange;
import pl.zankowski.iextrading4j.client.rest.manager.RestRequest;
import pl.zankowski.iextrading4j.client.rest.request.stocks.BatchStocksType;

import java.util.List;
import java.util.Map;

public interface IexClient {
    <R> R request(RestRequest<R> request);
    Map<String, BatchStocks> requestBatch(List<String> symbols, BatchStocksType type);

    ChartRange getRange();
    void setRange(ChartRange range);
}
