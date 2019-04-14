package com.josecponce.stockdata.iexdataloader.iex.jpaentities;

import com.josecponce.stockdata.iexdataloader.jpaaudit.Auditable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@IdClass(SplitEntity.SplitEntityId.class)
@Table(schema = "iex", catalog = "iex")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SplitEntity extends Auditable {
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


    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SplitEntityId implements Serializable {
        private String symbol;
        private LocalDate exDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SplitEntity that = (SplitEntity) o;
        return Objects.equals(symbol, that.symbol) &&
                Objects.equals(exDate, that.exDate) &&
                Objects.equals(declaredDate, that.declaredDate) &&
                Objects.equals(recordDate, that.recordDate) &&
                Objects.equals(paymentDate, that.paymentDate) &&
                Objects.equals(strip(ratio), strip(that.ratio)) &&
                Objects.equals(strip(toFactor), strip(that.toFactor)) &&
                Objects.equals(strip(forFactor), strip(that.forFactor));
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), symbol, exDate, declaredDate, recordDate, paymentDate, ratio, toFactor, forFactor);
    }
}
