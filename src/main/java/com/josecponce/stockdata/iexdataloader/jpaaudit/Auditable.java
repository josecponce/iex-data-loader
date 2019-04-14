package com.josecponce.stockdata.iexdataloader.jpaaudit;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public class Auditable implements Serializable {
    @LastModifiedDate
    @Column(name = "last_updated")
    private LocalDateTime lastUpdated;

    protected BigDecimal strip(BigDecimal number) {
        return number == null ? null : number.stripTrailingZeros();
    }
}
