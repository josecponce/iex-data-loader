package com.josecponce.stockdata.iexdataloader.iex.repositories;

import com.josecponce.stockdata.iexdataloader.iex.jpaentities.ExchangeSymbolEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExchangeSymbolRepository extends JpaRepository<ExchangeSymbolEntity, String> {
}
