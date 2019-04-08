package com.josecponce.stockdata.iexdataloader.iex.repositories;

import com.josecponce.stockdata.iexdataloader.iex.jpaentities.KeyStatsEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface KeyStatsRepository extends JpaRepository<KeyStatsEntity, String> {
}
