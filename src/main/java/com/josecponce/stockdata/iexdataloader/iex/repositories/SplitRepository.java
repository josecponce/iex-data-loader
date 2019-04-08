package com.josecponce.stockdata.iexdataloader.iex.repositories;

import com.josecponce.stockdata.iexdataloader.iex.jpaentities.SplitEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface SplitRepository extends JpaRepository<SplitEntity, SplitEntity.SplitEntityId> {
    List<SplitEntity> findAllBySymbolAndExDateBetween(String symbol, LocalDate exDateFrom, LocalDate exDateTo);
    Optional<SplitEntity> findFirstBySymbolOrderByExDateAsc(String symbol);
}
