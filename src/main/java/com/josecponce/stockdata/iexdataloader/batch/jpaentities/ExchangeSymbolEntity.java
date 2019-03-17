package com.josecponce.stockdata.iexdataloader.batch.jpaentities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(schema = "iex", catalog = "iex")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExchangeSymbolEntity {
    @Id
    private String symbol;
    private String name;
    private String date;
    @Column(name = "enabled")
    private String isEnabled;
    private String type;
    private String iexId;
    @UpdateTimestamp
    private LocalDateTime lastUpdated;
}
