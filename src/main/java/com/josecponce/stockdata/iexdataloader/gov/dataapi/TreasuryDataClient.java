package com.josecponce.stockdata.iexdataloader.gov.dataapi;

import com.josecponce.stockdata.iexdataloader.gov.jpaentities.TreasuryYieldEntity;

import java.time.LocalDateTime;
import java.util.List;

public interface TreasuryDataClient {
    List<TreasuryYieldEntity> requestLastNDays(int days);
    List<TreasuryYieldEntity> request(LocalDateTime dateUpper, LocalDateTime dateLower);
}
