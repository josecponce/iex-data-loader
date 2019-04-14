package com.josecponce.stockdata.iexdataloader.gov.dataapi;

import com.josecponce.stockdata.iexdataloader.gov.jpaentities.TreasuryYieldEntity;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@Slf4j
public class TreasuryDataClientImpl implements TreasuryDataClient {
    private static final String ROOT_URI = "https://data.treasury.gov";
    private static final String FILTER_DATE_FORMAT = "NEW_DATE ge DateTime'%s' and NEW_DATE le DateTime'%s'";
    private final RestTemplate client;

    public TreasuryDataClientImpl(RestTemplateBuilder templateBuilder) {
        this.client = templateBuilder.rootUri(ROOT_URI).build();
    }

    @Override
    public List<TreasuryYieldEntity> requestLastNDays(int days) {
        val dateUpper = LocalDateTime.now();
        val dateLower = LocalDateTime.now().minus(Duration.ofDays(days));
        return request(dateUpper, dateLower);
    }

    @Override
    public List<TreasuryYieldEntity> request(LocalDateTime dateUpper, LocalDateTime dateLower) {
        val filter = String.format(FILTER_DATE_FORMAT, dateLower, dateUpper);
        val uri = UriComponentsBuilder.fromUriString("/feed.svc/DailyTreasuryYieldCurveRateData").queryParam("$filter", filter).build().toUriString();
        try {
            ResponseEntity<TreasuryYieldFeed> feed = client.getForEntity(uri, TreasuryYieldFeed.class);
            if (Objects.equals(feed.getHeaders().getContentType(), MediaType.APPLICATION_ATOM_XML)) {
                return processXmlOutput(feed);
            } else if (Objects.equals(feed.getHeaders().getContentType(), MediaType.APPLICATION_JSON_UTF8)) {
                ResponseEntity<TreasuryYieldFeedJson> feedJson = client.getForEntity(uri, TreasuryYieldFeedJson.class);
                return processJsonOutput(feedJson);
            } else {
                throw new IllegalStateException(String.format("The treasury api has returned a response in an unrecognized format: %s",
                        feed.getHeaders().getContentType()));
            }
        } catch (HttpStatusCodeException e) {
            log.error("Failed to retrieve treasury yields with filter '{}' with status code '{}'", filter, e.getStatusCode());
            throw e;
        }
    }

    private List<TreasuryYieldEntity> processJsonOutput(ResponseEntity<TreasuryYieldFeedJson> feedJson) {
        return Objects.requireNonNull(feedJson.getBody()).getEntries().stream()
                .flatMap(entry -> {
                    val matcher = Pattern.compile("/Date\\(([0-9]*)\\)/").matcher(entry.getDate());
                    String date;
                    if (matcher.matches()) {
                        date = Instant.ofEpochMilli(Long.parseLong(matcher.group(1))).toString();
                    } else {
                        log.error("Failed to parse date from json output for treasury data '{}'", entry.getDate());
                        date = entry.getDate(); //don't fail, just log it
                    }
                    return createEntities(date, new Double[]{
                            entry.getMonth1(),
                            entry.getMonth2(),
                            entry.getMonth3(),
                            entry.getMonth6(),
                            entry.getYear1(),
                            entry.getYear2(),
                            entry.getYear3(),
                            entry.getYear5(),
                            entry.getYear7(),
                            entry.getYear10(),
                            entry.getYear20(),
                            entry.getYear30()
                    });
                }).collect(Collectors.toList());
    }

    private List<TreasuryYieldEntity> processXmlOutput(ResponseEntity<TreasuryYieldFeed> feed) {
        return Objects.requireNonNull(feed.getBody()).getEntries().stream().map(entry -> entry.getContent().getProperties())
                .flatMap(entry -> createEntities(entry.getDate(), new Double[]{
                                entry.getMonth1().getYield(),
                                entry.getMonth2().getYield(),
                                entry.getMonth3().getYield(),
                                entry.getMonth6().getYield(),
                                entry.getYear1().getYield(),
                                entry.getYear2().getYield(),
                                entry.getYear3().getYield(),
                                entry.getYear5().getYield(),
                                entry.getYear7().getYield(),
                                entry.getYear10().getYield(),
                                entry.getYear20().getYield(),
                                entry.getYear30().getYield()
                        })
                ).collect(Collectors.toList());
    }

    private Stream<TreasuryYieldEntity> createEntities(String date, Double[] yields) {
        return Stream.of(
                new TreasuryYieldEntity(date, "1MONTH", yields[0] == null ? null : BigDecimal.valueOf(yields[0]), 1),
                new TreasuryYieldEntity(date, "2MONTH", yields[1] == null ? null : BigDecimal.valueOf(yields[1]), 2),
                new TreasuryYieldEntity(date, "3MONTH", yields[2] == null ? null : BigDecimal.valueOf(yields[2]), 3),
                new TreasuryYieldEntity(date, "6MONTH", yields[3] == null ? null : BigDecimal.valueOf(yields[3]), 4),
                new TreasuryYieldEntity(date, "1YEAR", yields[4] == null ? null : BigDecimal.valueOf(yields[4]), 5),
                new TreasuryYieldEntity(date, "2YEAR", yields[5] == null ? null : BigDecimal.valueOf(yields[5]), 6),
                new TreasuryYieldEntity(date, "3YEAR", yields[6] == null ? null : BigDecimal.valueOf(yields[6]), 7),
                new TreasuryYieldEntity(date, "5YEAR", yields[7] == null ? null : BigDecimal.valueOf(yields[7]), 8),
                new TreasuryYieldEntity(date, "7YEAR", yields[8] == null ? null : BigDecimal.valueOf(yields[8]), 9),
                new TreasuryYieldEntity(date, "10YEAR", yields[9] == null ? null : BigDecimal.valueOf(yields[9]), 10),
                new TreasuryYieldEntity(date, "20YEAR", yields[10] == null ? null : BigDecimal.valueOf(yields[10]), 11),
                new TreasuryYieldEntity(date, "30YEAR", yields[11] == null ? null : BigDecimal.valueOf(yields[11]), 12)
        );
    }
}
