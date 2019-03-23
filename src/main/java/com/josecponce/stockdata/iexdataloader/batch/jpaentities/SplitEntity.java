package com.josecponce.stockdata.iexdataloader.batch.jpaentities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@IdClass(SplitEntity.SplitEntityId.class)
@Table(schema = "iex", catalog = "iex")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SplitEntity {
    @Id
    private String symbol;
    @Id
    private LocalDate exDate;
    private LocalDate declaredDate;
    private LocalDate recordDate;
    private LocalDate paymentDate;
    private BigDecimal ratio;
    private BigDecimal toFactor;
    private BigDecimal forFactor;
    @UpdateTimestamp
    private LocalDateTime lastUpdated;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SplitEntityId implements Serializable {
        private String symbol;
        private LocalDate exDate;
    }
}
