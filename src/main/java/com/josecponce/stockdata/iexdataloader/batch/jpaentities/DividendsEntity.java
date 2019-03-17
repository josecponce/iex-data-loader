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
    private String exDate;
    private String paymentDate;
    private String recordDate;
    private String declaredDate;
    private String amount;
    private String flag;
    private String type;
    private String qualified;
    private String indicated;
    @UpdateTimestamp
    private LocalDateTime lastUpdated;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DividendsEntityId implements Serializable {
        private String symbol;
        private String exDate;
    }
}