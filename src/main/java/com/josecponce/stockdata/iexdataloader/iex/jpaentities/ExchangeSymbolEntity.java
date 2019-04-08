package com.josecponce.stockdata.iexdataloader.iex.jpaentities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UpdateTimestamp;
import pl.zankowski.iextrading4j.api.refdata.SymbolType;

import javax.persistence.*;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(schema = "iex", catalog = "iex")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExchangeSymbolEntity {
    @Id
    private String symbol;
    private String name;
    private LocalDate date;
    private Boolean enabled;
    @Enumerated(EnumType.STRING)
    private SymbolType type;
    private Long iexId;
    @UpdateTimestamp
    private LocalDateTime lastUpdated;

    public String getSymbolEncoded() {
        try {
            return URLEncoder.encode(symbol, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }
}
