package com.josecponce.stockdata.iexdataloader.batch.repositories;

import com.josecponce.stockdata.iexdataloader.batch.jpaentities.KeyStatsEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface KeyStatsRepository extends JpaRepository<KeyStatsEntity, String> {
}
