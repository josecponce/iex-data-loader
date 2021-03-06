package com.josecponce.stockdata.iexdataloader.gov.jpaentities;

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

@EqualsAndHashCode(callSuper = false)
@Entity
@IdClass(TreasuryYieldEntity.TreasuryYieldId.class)
@Table(schema = "iex", catalog = "iex")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TreasuryYieldEntity extends Auditable {
    @Id
    private String date;
    @Id
    private String maturity;
    private BigDecimal yield;
    private Integer position;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TreasuryYieldId implements Serializable {
        private String date;
        private String maturity;
    }
}
