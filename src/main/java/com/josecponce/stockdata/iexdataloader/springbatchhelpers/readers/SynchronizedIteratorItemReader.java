package com.josecponce.stockdata.iexdataloader.springbatchhelpers.readers;

import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;
import org.springframework.batch.item.support.IteratorItemReader;

import java.util.Iterator;

public class SynchronizedIteratorItemReader<T> extends IteratorItemReader<T> {

    public SynchronizedIteratorItemReader(Iterable<T> iterable) {
        super(iterable);
    }

    public SynchronizedIteratorItemReader(Iterator<T> iterator) {
        super(iterator);
    }

    @Override
    public synchronized T read() throws Exception, UnexpectedInputException, ParseException {
        return super.read();
    }
}
