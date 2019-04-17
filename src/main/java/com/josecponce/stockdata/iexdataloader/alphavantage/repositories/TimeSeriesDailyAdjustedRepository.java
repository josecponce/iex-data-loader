package com.josecponce.stockdata.iexdataloader.alphavantage.repositories;

import com.josecponce.stockdata.iexdataloader.alphavantage.jpaentities.TimeSeriesDailyAdjustedEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TimeSeriesDailyAdjustedRepository extends JpaRepository<TimeSeriesDailyAdjustedEntity, TimeSeriesDailyAdjustedEntity.TimeSeriesDailyAdjustedId> {
}
