package com.josecponce.stockdata.iexdataloader.gov.jpaentities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@IdClass(TreasuryYieldEntity.TreasuryYieldId.class)
@Table(schema = "treasury", catalog = "treasury")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TreasuryYieldEntity {
    @Id
    private String date;
    @Id
    private String maturity;
    private Double yield;
    private Integer position;

    @UpdateTimestamp
    private LocalDateTime lastUpdated;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TreasuryYieldId implements Serializable {
        private String date;
        private String maturity;
    }
}
