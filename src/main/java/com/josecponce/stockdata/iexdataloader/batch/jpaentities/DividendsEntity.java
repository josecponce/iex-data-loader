package com.josecponce.stockdata.iexdataloader.batch.jpaentities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UpdateTimestamp;
import pl.zankowski.iextrading4j.api.stocks.DividendQualification;
import pl.zankowski.iextrading4j.api.stocks.DividendType;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@IdClass(DividendsEntity.DividendsEntityId.class)
@Table(schema = "iex", catalog = "iex")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DividendsEntity implements Serializable {
    @Id
    private String symbol;
    @Id
    private LocalDate exDate;
    private LocalDate paymentDate;
    private LocalDate recordDate;
    private LocalDate declaredDate;
    private BigDecimal amount;
    private String flag;
    private DividendType type;
    private DividendQualification qualified;
    private String indicated;
    @UpdateTimestamp
    private LocalDateTime lastUpdated;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DividendsEntityId implements Serializable {
        private String symbol;
        private LocalDate exDate;
    }
}