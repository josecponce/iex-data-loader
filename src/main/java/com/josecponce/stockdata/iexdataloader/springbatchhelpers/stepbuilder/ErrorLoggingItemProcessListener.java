package com.josecponce.stockdata.iexdataloader.springbatchhelpers.stepbuilder;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.ItemProcessListener;

@Slf4j
public class ErrorLoggingItemProcessListener implements ItemProcessListener {
    @Override
    public void beforeProcess(Object item) {

    }

    @Override
    public void afterProcess(Object item, Object result) {

    }

    @Override
    public void onProcessError(Object item, Exception e) {
        log.error("Failed to process item {} with exception '{}' of type {}", item, e.getMessage(),
                e.getClass().getSimpleName());
    }
}