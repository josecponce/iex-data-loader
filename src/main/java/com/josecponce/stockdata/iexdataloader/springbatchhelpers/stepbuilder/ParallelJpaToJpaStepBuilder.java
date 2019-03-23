package com.josecponce.stockdata.iexdataloader.springbatchhelpers.stepbuilder;

import com.josecponce.stockdata.iexdataloader.springbatchhelpers.CompositeList;
import lombok.AllArgsConstructor;
import lombok.experimental.Wither;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.hibernate.exception.LockAcquisitionException;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.step.builder.FaultTolerantStepBuilder;
import org.springframework.batch.core.step.builder.SimpleStepBuilder;
import org.springframework.batch.item.*;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.batch.item.database.builder.JpaPagingItemReaderBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.core.GenericTypeResolver;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManagerFactory;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Component
@AllArgsConstructor
@Scope("prototype")
@Slf4j
public class ParallelJpaToJpaStepBuilder<In, Out> {
    @Wither
    private String name;
    /**
     * This can never be set as a lambda, it has to be set as an annonymous inner class
     * because lambdas lose their type parameters to type erasure at compile time.
     */
    @Wither
    private ItemProcessor<In, Out> processor;
    @Wither
    private ItemReader<In> reader;

    @Wither
    private int chunk = 100;
    @Wither
    private int concurrency = 1;
    @Wither
    private String orderBy = "";
    @Wither
    private Class<Out> outClass;
    @Wither
    private Class<?> inComponentClass;
    @Wither
    private Class<In> inClass;
    @Wither
    private Collection<Class<? extends Throwable>> skip = new ArrayList<>();

    private final EntityManagerFactory entityManagerFactory;
    private final PlatformTransactionManager transactionManager;
    private final TaskExecutor executor;
    private final StepBuilderFactory stepBuilder;
    private final JpaItemWriter jpaWriter;

    @Autowired
    public ParallelJpaToJpaStepBuilder(EntityManagerFactory entityManagerFactory,
                                       PlatformTransactionManager transactionManager,
                                       TaskExecutor executor, StepBuilderFactory stepBuilder, JpaItemWriter jpaWriter) {
        this.entityManagerFactory = entityManagerFactory;
        this.transactionManager = transactionManager;
        this.executor = executor;
        this.stepBuilder = stepBuilder;
        this.jpaWriter = jpaWriter;
    }

    @SuppressWarnings("unchecked")
    public Step build() {
        Class[] types = processor == null || GenericTypeResolver.resolveTypeArguments(processor.getClass(), ItemProcessor.class) == null
                ? new Class[]{null, null}
                : GenericTypeResolver.resolveTypeArguments(processor.getClass(), ItemProcessor.class);
        types[0] = inClass != null ? inClass : types[0];
        types[1] = outClass != null ? outClass : types[1];

        if (types == null) {
            throw new RuntimeException("The processor needs to be set as an anonymous inner class.");
        }

        ItemReader baseReader = this.reader != null ? this.reader : new JpaPagingItemReaderBuilder().name(name + "Reader")
                .entityManagerFactory(entityManagerFactory).saveState(false).pageSize(chunk)
                .queryString(String.format("SELECT item FROM %s item %s",
                        inComponentClass != null ? inComponentClass.getSimpleName() : types[0].getSimpleName(),
                        StringUtils.isEmpty(orderBy) ? "" : "ORDER BY " + orderBy)).build();

        ItemWriter writer = !List.class.isAssignableFrom(types[1]) ? jpaWriter : items -> jpaWriter.write(new CompositeList(items));
        ItemReader reader = !List.class.isAssignableFrom(types[0]) ? baseReader :
                new ItemStreamReader<List>() {
                    private JpaPagingItemReader delegate = (JpaPagingItemReader) baseReader;

                    @Override
                    public synchronized List read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
                        List<Object> batch = IntStream.range(0, chunk)
                                .mapToObj(i -> {
                                    try {
                                        return delegate.read();
                                    } catch (Exception e) {
                                        throw new RuntimeException(e);
                                    }
                                }).filter(Objects::nonNull).collect(Collectors.toList());
                        return batch.isEmpty() ? null : batch;
                    }

                    @Override
                    public void open(ExecutionContext executionContext) throws ItemStreamException {
                        delegate.open(executionContext);
                    }

                    @Override
                    public void update(ExecutionContext executionContext) throws ItemStreamException {
                        delegate.update(executionContext);
                    }

                    @Override
                    public void close() throws ItemStreamException {
                        delegate.close();
                    }
                };

        FaultTolerantStepBuilder intermediateBuilder = stepBuilder.get(name)
                .transactionManager(transactionManager)
                .chunk(reader == baseReader ? chunk : 1)
                .faultTolerant()
                .retry(LockAcquisitionException.class).retryLimit(5)
                .processorNonTransactional();
        for (val throwable : skip) {
            intermediateBuilder = intermediateBuilder.skip(throwable).skipLimit(1000);
        }
        SimpleStepBuilder simpleStepBuilder = intermediateBuilder
                .reader(reader)
                .listener(new ErrorLoggingItemProcessListener())
                .writer(writer);
        if (processor != null) {
            simpleStepBuilder = simpleStepBuilder.processor(processor);
        }
        return simpleStepBuilder.taskExecutor(executor).throttleLimit(concurrency).build();
    }
}
