package com.josecponce.stockdata.iexdataloader.alphavantage.jpaentities;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.josecponce.stockdata.iexdataloader.jpaaudit.Auditable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.util.Objects;

@EqualsAndHashCode(callSuper = false)
@Entity
@IdClass(TimeSeriesDailyAdjustedEntity.TimeSeriesDailyAdjustedId.class)
@Table(catalog = "iex", schema = "iex")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TimeSeriesDailyAdjustedEntity extends Auditable {
    @Id
    private String symbol;
    @Id
    private LocalDate date;
    private BigDecimal open;
    private BigDecimal high;
    private BigDecimal low;
    private BigDecimal close;
    private BigDecimal adjustedClose;
    private BigInteger volume;
    private BigDecimal dividendAmount;
    private BigDecimal splitCoefficient;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TimeSeriesDailyAdjustedId implements Serializable {
        private String symbol;
        private LocalDate date;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TimeSeriesDailyAdjustedEntity that = (TimeSeriesDailyAdjustedEntity) o;
        return Objects.equals(symbol, that.symbol) &&
                Objects.equals(date, that.date) &&
                Objects.equals(strip(open), strip(that.open)) &&
                Objects.equals(strip(high), strip(that.high)) &&
                Objects.equals(strip(low), strip(that.low)) &&
                Objects.equals(strip(close), strip(that.close)) &&
                Objects.equals(strip(adjustedClose), strip(that.adjustedClose)) &&
                Objects.equals(volume, that.volume) &&
                Objects.equals(strip(dividendAmount), strip(that.dividendAmount)) &&
                Objects.equals(strip(splitCoefficient), strip(that.splitCoefficient));
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), symbol, date, open, high, low, close, adjustedClose, volume, dividendAmount, splitCoefficient);
    }
}
