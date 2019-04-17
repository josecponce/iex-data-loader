package com.josecponce.stockdata.iexdataloader.management.repositories;

import com.josecponce.stockdata.iexdataloader.management.jpaentities.AlphaVantageLoadStockEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AlphaVantageLoadStockRepository extends JpaRepository<AlphaVantageLoadStockEntity, String> {
}
