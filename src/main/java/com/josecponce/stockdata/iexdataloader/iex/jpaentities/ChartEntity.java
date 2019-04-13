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

@EqualsAndHashCode(callSuper = true)
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
}
