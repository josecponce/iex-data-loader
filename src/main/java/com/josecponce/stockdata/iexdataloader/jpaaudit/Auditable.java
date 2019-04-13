package com.josecponce.stockdata.iexdataloader.jpaaudit;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public class Auditable implements Serializable {
    @LastModifiedDate
    @Column(name = "last_updated")
    private LocalDateTime lastUpdated;
    @CreatedDate
    @Column(name = "created")
    private LocalDateTime created;
}
