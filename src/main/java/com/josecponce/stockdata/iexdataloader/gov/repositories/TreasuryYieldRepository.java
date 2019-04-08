package com.josecponce.stockdata.iexdataloader.gov.repositories;

import com.josecponce.stockdata.iexdataloader.gov.jpaentities.TreasuryYieldEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TreasuryYieldRepository extends JpaRepository<TreasuryYieldEntity, TreasuryYieldEntity.TreasuryYieldId> {
}
