package com.josecponce.stockdata.iexdataloader.management.jpaentities;

import com.josecponce.stockdata.iexdataloader.jpaaudit.Auditable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@EqualsAndHashCode(callSuper = false)
@Entity
@Table(catalog = "management", schema = "management")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AlphaVantageLoadStockEntity extends Auditable {
    @Id
    private String symbol;
}
