package com.josecponce.stockdata.iexdataloader.gov.dataapi;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlText;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@JacksonXmlRootElement(localName = "feed")
@JsonIgnoreProperties({"title", "link", "base"})
public class TreasuryYieldFeed {
    private String id;
    private String updated;
    @JacksonXmlProperty(localName = "entry")
    @JacksonXmlElementWrapper(useWrapping = false)
    private List<Entry> entries = new ArrayList<>();

    public void setEntries(Entry entries) {
        this.entries.add(entries);
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @JsonIgnoreProperties({"title", "author", "link", "category"})
    public static class Entry {
        private String id;
        private String updated;
        private Content content;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @JsonIgnoreProperties({"type"})
    public static class Content {
        private Properties properties;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Properties {
        @JacksonXmlProperty(localName = "Id")
        private String id;
        @JacksonXmlProperty(localName = "NEW_DATE")
        private String date;
        @JacksonXmlProperty(localName = "BC_1MONTH")
        private Yield month1;
        @JacksonXmlProperty(localName = "BC_2MONTH")
        private Yield month2;
        @JacksonXmlProperty(localName = "BC_3MONTH")
        private Yield month3;
        @JacksonXmlProperty(localName = "BC_6MONTH")
        private Yield month6;
        @JacksonXmlProperty(localName = "BC_1YEAR")
        private Yield year1;
        @JacksonXmlProperty(localName = "BC_2YEAR")
        private Yield year2;
        @JacksonXmlProperty(localName = "BC_3YEAR")
        private Yield year3;
        @JacksonXmlProperty(localName = "BC_5YEAR")
        private Yield year5;
        @JacksonXmlProperty(localName = "BC_7YEAR")
        private Yield year7;
        @JacksonXmlProperty(localName = "BC_10YEAR")
        private Yield year10;
        @JacksonXmlProperty(localName = "BC_20YEAR")
        private Yield year20;
        @JacksonXmlProperty(localName = "BC_30YEAR")
        private Yield year30;
        @JacksonXmlProperty(localName = "BC_30YEARDISPLAY")
        private Yield year30Display;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @JsonIgnoreProperties("type")
    public static class Yield {
        @JacksonXmlText
        private Double yield;
    }
}