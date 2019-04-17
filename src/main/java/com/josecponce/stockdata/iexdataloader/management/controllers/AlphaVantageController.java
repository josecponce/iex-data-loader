package com.josecponce.stockdata.iexdataloader.management.controllers;

import com.josecponce.stockdata.iexdataloader.ScheduledBatchJobs;
import com.josecponce.stockdata.iexdataloader.management.jpaentities.AlphaVantageLoadStockEntity;
import com.josecponce.stockdata.iexdataloader.management.repositories.AlphaVantageLoadStockRepository;
import lombok.val;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

@RestController
@RequestMapping("/alphaVantage")
public class AlphaVantageController {

    private Executor executor = Executors.newFixedThreadPool(10);
    private final AlphaVantageLoadStockRepository repository;
    private final ScheduledBatchJobs jobs;

    public AlphaVantageController(AlphaVantageLoadStockRepository repository, ScheduledBatchJobs jobs) {
        this.repository = repository;
        this.jobs = jobs;
    }

    @GetMapping(value = "/addSymbol/{symbol}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public AlphaVantageLoadStockEntity addSymbol(@PathVariable String symbol) {
        val entity = repository.save(new AlphaVantageLoadStockEntity(symbol));
        executor.execute(jobs::alphaVantageJob);
        return entity;
    }
}
