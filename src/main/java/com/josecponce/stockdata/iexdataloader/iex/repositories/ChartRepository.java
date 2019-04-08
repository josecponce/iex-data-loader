package com.josecponce.stockdata.iexdataloader.iex.repositories;

import com.josecponce.stockdata.iexdataloader.iex.jpaentities.ChartEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChartRepository extends JpaRepository<ChartEntity, ChartEntity.ChartEntityId> {
}
