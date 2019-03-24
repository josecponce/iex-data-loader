package com.josecponce.stockdata.iexdataloader.batch.repositories;

import com.josecponce.stockdata.iexdataloader.batch.jpaentities.ChartEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChartRepository extends JpaRepository<ChartEntity, ChartEntity.ChartEntityId> {
}
