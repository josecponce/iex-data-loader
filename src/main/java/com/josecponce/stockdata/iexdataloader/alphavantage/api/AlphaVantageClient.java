package com.josecponce.stockdata.iexdataloader.alphavantage.api;

public interface AlphaVantageClient {
    TimeSeriesDailyAdjustedDTO requestTimeSeriesDailyAdjusted(String symbol, OutputSize size);
}
