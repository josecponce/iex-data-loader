package com.josecponce.stockdata.iexdataloader.gov.dataapi;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TreasuryYieldFeedJson {
    @JsonProperty("d")
    private List<Entry> entries;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonIgnoreProperties("__metadata")
    public static class Entry {
        @JsonProperty("Id")
        private long id;
        @JsonProperty("NEW_DATE")
        private String date;
        @JsonProperty("BC_1MONTH")
        private Double month1;
        @JsonProperty("BC_2MONTH")
        private Double month2;
        @JsonProperty("BC_3MONTH")
        private Double month3;
        @JsonProperty("BC_6MONTH")
        private Double month6;
        @JsonProperty("BC_1YEAR")
        private Double year1;
        @JsonProperty("BC_2YEAR")
        private Double year2;
        @JsonProperty("BC_3YEAR")
        private Double year3;
        @JsonProperty("BC_5YEAR")
        private Double year5;
        @JsonProperty("BC_7YEAR")
        private Double year7;
        @JsonProperty("BC_10YEAR")
        private Double year10;
        @JsonProperty("BC_20YEAR")
        private Double year20;
        @JsonProperty("BC_30YEAR")
        private Double year30;
        @JsonProperty("BC_30YEARDISPLAY")
        private Double year30Display;
    }
}

