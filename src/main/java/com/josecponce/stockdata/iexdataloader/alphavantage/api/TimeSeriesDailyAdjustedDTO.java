package com.josecponce.stockdata.iexdataloader.alphavantage.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.josecponce.stockdata.iexdataloader.alphavantage.jpaentities.TimeSeriesDailyAdjustedEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TimeSeriesDailyAdjustedDTO {
    @JsonProperty("Meta Data")
    private Metadata metadata;
    @JsonProperty("Time Series (Daily)")
    private Map<LocalDate, TimeSeriesDailyAdjusted> data;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Metadata {
        @JsonProperty("1. Information")
        private String information;
        @JsonProperty("2. Symbol")
        private String symbol;
        @JsonProperty("3. Last Refreshed")
        private String lastRefreshed;
        @JsonProperty("4. Output Size")
        private String outputSize;
        @JsonProperty("5. Time Zone")
        private String timezone;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class TimeSeriesDailyAdjusted {
        @JsonProperty("1. open")
        private BigDecimal open;
        @JsonProperty("2. high")
        private BigDecimal high;
        @JsonProperty("3. low")
        private BigDecimal low;
        @JsonProperty("4. close")
        private BigDecimal close;
        @JsonProperty("5. adjusted close")
        private BigDecimal adjustedClose;
        @JsonProperty("6. volume")
        private BigInteger volume;
        @JsonProperty("7. dividend amount")
        private BigDecimal dividendAmount;
        @JsonProperty("8. split coefficient")
        private BigDecimal splitCoefficient;
    }

    public Stream<TimeSeriesDailyAdjustedEntity> toEntities() {
        return data.entrySet().stream().map(entry ->
        {
            TimeSeriesDailyAdjusted value = entry.getValue();
            return new TimeSeriesDailyAdjustedEntity(metadata.getSymbol(), entry.getKey(), value.getOpen(),
                    value.getHigh(), value.getLow(), value.getClose(), value.getAdjustedClose(), value.getVolume(),
                    value.getDividendAmount(), value.getSplitCoefficient());
        });
    }
}
