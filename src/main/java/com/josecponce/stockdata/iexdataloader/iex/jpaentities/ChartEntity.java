package com.josecponce.stockdata.iexdataloader.iex.jpaentities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@IdClass(ChartEntity.ChartEntityId.class)
@Table(schema = "iex", catalog = "iex")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChartEntity implements Serializable {
    @Id
    private String symbol;
    @Id
    private String date;
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

    @UpdateTimestamp
    private LocalDateTime lastUpdated;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ChartEntityId implements Serializable {
        private String symbol;
        private String date;
    }
}
