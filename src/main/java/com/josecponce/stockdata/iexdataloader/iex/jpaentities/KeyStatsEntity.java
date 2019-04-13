package com.josecponce.stockdata.iexdataloader.iex.jpaentities;

import com.josecponce.stockdata.iexdataloader.jpaaudit.Auditable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(schema = "iex", catalog = "iex")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class KeyStatsEntity extends Auditable {
    @Id
    private String symbol;

    private String companyName;
    private BigDecimal marketcap;
    private BigDecimal beta;
    private BigDecimal week52high;
    private BigDecimal week52low;
    private BigDecimal week52change;
    private BigDecimal shortInterest;
    private LocalDate shortDate;
    private BigDecimal dividendRate;
    private BigDecimal dividendYield;
    private LocalDateTime exDividendDate;
    private BigDecimal latestEPS;
    private LocalDate latestEPSDate;
    private BigDecimal sharesOutstanding;
    @Column(name = "_float")
    private BigDecimal Float;
    private BigDecimal returnOnEquity;
    private BigDecimal consensusEPS;
    private BigDecimal numberOfEstimates;
    private BigDecimal EPSSurpriseDollar;
    private BigDecimal EPSSurprisePercent;
    private BigDecimal EBITDA;
    private BigDecimal revenue;
    private BigDecimal grossProfit;
    private BigDecimal cash;
    private BigDecimal debt;
    private BigDecimal ttmEPS;
    private BigDecimal revenuePerShare;
    private BigDecimal revenuePerEmployee;
    private BigDecimal peRatioHigh;
    private BigDecimal peRatioLow;
    private BigDecimal returnOnAssets;
    private BigDecimal returnOnCapital;
    private BigDecimal profitMargin;
    private BigDecimal priceToSales;
    private BigDecimal priceToBook;
    private BigDecimal day200MovingAvg;
    private BigDecimal day50MovingAvg;
    private BigDecimal institutionPercent;
    private BigDecimal insiderPercent;
    private BigDecimal shortRatio;
    private BigDecimal year5ChangePercent;
    private BigDecimal year2ChangePercent;
    private BigDecimal year1ChangePercent;
    private BigDecimal ytdChangePercent;
    private BigDecimal month6ChangePercent;
    private BigDecimal month3ChangePercent;
    private BigDecimal month1ChangePercent;
    private BigDecimal day5ChangePercent;
    private BigDecimal day30ChangePercent;
}
