package com.josecponce.stockdata.iexdataloader.batch.repositories;

import com.josecponce.stockdata.iexdataloader.batch.jpaentities.ExchangeSymbolEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExchangeSymbolRepository extends JpaRepository<ExchangeSymbolEntity, String> {
}
