package com.josecponce.stockdata.iexdataloader.iex.jpaentities;

import com.josecponce.stockdata.iexdataloader.jpaaudit.Auditable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

@Entity
@IdClass(ChartEntity.ChartEntityId.class)
@Table(schema = "iex", catalog = "iex")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChartEntity extends Auditable {
    @Id
    private String symbol;
    @Id
    private String date;
    private LocalDate dateDate;
    private BigDecimal open;
    private BigDecimal high;
    private BigDecimal low;
    private BigDecimal close;
    private BigDecimal volume;
    private BigDecimal unadjustedVolume;
    @Column(name = "_change")
    private BigDecimal change;
    private BigDecimal changePercent;
    private BigDecimal vwap;
    private String label;
    private BigDecimal changeOverTime;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ChartEntityId implements Serializable {
        private String symbol;
        private String date;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChartEntity that = (ChartEntity) o;
        return Objects.equals(symbol, that.symbol) &&
                Objects.equals(date, that.date) &&
                Objects.equals(dateDate, that.dateDate) &&
                Objects.equals(strip(open), strip(that.open)) &&
                Objects.equals(strip(high), strip(that.high)) &&
                Objects.equals(strip(low), strip(that.low)) &&
                Objects.equals(strip(close), strip(that.close)) &&
                Objects.equals(strip(volume), strip(that.volume)) &&
                Objects.equals(strip(unadjustedVolume), strip(that.unadjustedVolume)) &&
                Objects.equals(strip(change), strip(that.change)) &&
                Objects.equals(strip(changePercent), strip(that.changePercent)) &&
                Objects.equals(strip(vwap), strip(that.vwap)) &&
                Objects.equals(label, that.label) &&
                Objects.equals(strip(changeOverTime), strip(that.changeOverTime));
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), symbol, date, dateDate, open, high, low, close, volume, unadjustedVolume, change, changePercent, vwap, label, changeOverTime);
    }
}
