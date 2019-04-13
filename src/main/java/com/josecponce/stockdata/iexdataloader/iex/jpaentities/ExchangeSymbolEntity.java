package com.josecponce.stockdata.iexdataloader.iex.jpaentities;

import com.josecponce.stockdata.iexdataloader.jpaaudit.Auditable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import pl.zankowski.iextrading4j.api.refdata.SymbolType;

import javax.persistence.*;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.time.LocalDate;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(schema = "iex", catalog = "iex")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExchangeSymbolEntity extends Auditable {
    @Id
    private String symbol;
    private String name;
    private LocalDate date;
    private Boolean enabled;
    @Enumerated(EnumType.STRING)
    private SymbolType type;
    private Long iexId;

    public String getSymbolEncoded() {
        try {
            return URLEncoder.encode(symbol, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }
}
