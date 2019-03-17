package com.josecponce.stockdata.iexdataloader.batch.jpaentities;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(schema = "iex", catalog = "iex")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class KeyStatsEntity {
    @Id
    private String symbol;
    private String companyName;
    private String marketcap;
    private String beta;
    private String week52high;
    private String week52low;
    private String week52change;
    private String shortInterest;
    private String shortDate;
    private String dividendRate;
    private String dividendYield;
    private String exDividendDate;
    private String latestEPS;
    private String latestEPSDate;
    private String sharesOutstanding;
    @Column(name = "_float")
    private String Float;
    private String returnOnEquity;
    private String consensusEPS;
    private String numberOfEstimates;
    @JsonProperty("EPSSurpriseDollar")
    private String EPSSurpriseDollar;
    @JsonProperty("EPSSurprisePercent")
    private String ePSSurprisePercent;
    @JsonProperty("EBITDA")
    private String eBITDA;
    private String revenue;
    private String grossProfit;
    private String cash;
    private String debt;
    private String ttmEPS;
    private String revenuePerShare;
    private String revenuePerEmployee;
    private String peRatioHigh;
    private String peRatioLow;
    private String returnOnAssets;
    private String returnOnCapital;
    private String profitMargin;
    private String priceToSales;
    private String priceToBook;
    private String day200MovingAvg;
    private String day50MovingAvg;
    private String institutionPercent;
    private String insiderPercent;
    private String shortRatio;
    private String year5ChangePercent;
    private String year2ChangePercent;
    private String year1ChangePercent;
    private String ytdChangePercent;
    private String month6ChangePercent;
    private String month3ChangePercent;
    private String month1ChangePercent;
    private String day5ChangePercent;
    private String day30ChangePercent;
    @UpdateTimestamp
    private LocalDateTime lastUpdated;
}
