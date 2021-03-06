package com.josecponce.stockdata.iexdataloader.iex.jpaentities;

import com.josecponce.stockdata.iexdataloader.jpaaudit.Auditable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UpdateTimestamp;
import pl.zankowski.iextrading4j.api.stocks.DividendQualification;
import pl.zankowski.iextrading4j.api.stocks.DividendType;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@IdClass(DividendsEntity.DividendsEntityId.class)
@Table(schema = "iex", catalog = "iex")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DividendsEntity extends Auditable {
    @Id
    private String symbol;
    @Id
    private LocalDate exDate;
    private LocalDate paymentDate;
    private LocalDate recordDate;
    private LocalDate declaredDate;
    private BigDecimal amount;
    private String flag;
    @Enumerated(EnumType.STRING)
    private DividendType type;
    @Enumerated(EnumType.STRING)
    private DividendQualification qualified;
    private String indicated;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DividendsEntityId implements Serializable {
        private String symbol;
        private LocalDate exDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DividendsEntity that = (DividendsEntity) o;
        return Objects.equals(symbol, that.symbol) &&
                Objects.equals(exDate, that.exDate) &&
                Objects.equals(paymentDate, that.paymentDate) &&
                Objects.equals(recordDate, that.recordDate) &&
                Objects.equals(declaredDate, that.declaredDate) &&
                Objects.equals(strip(amount), strip(that.amount)) &&
                Objects.equals(flag, that.flag) &&
                type == that.type &&
                qualified == that.qualified &&
                Objects.equals(indicated, that.indicated);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), symbol, exDate, paymentDate, recordDate, declaredDate, amount, flag, type, qualified, indicated);
    }
}