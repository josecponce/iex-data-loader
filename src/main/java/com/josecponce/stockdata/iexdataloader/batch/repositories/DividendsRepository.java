package com.josecponce.stockdata.iexdataloader.batch.repositories;

import com.josecponce.stockdata.iexdataloader.batch.jpaentities.DividendsEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface DividendsRepository extends JpaRepository<DividendsEntity, DividendsEntity.DividendsEntityId> {
    public List<DividendsEntity> findAllBySymbolAndExDateBetween(String symbol, LocalDate exDateFrom, LocalDate exDateTo);
}
