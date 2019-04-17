package com.josecponce.stockdata.iexdataloader;

import com.josecponce.stockdata.iexdataloader.alphavantage.AlphaVantageBatchConfiguration;
import com.josecponce.stockdata.iexdataloader.gov.GovBatchConfiguration;
import com.josecponce.stockdata.iexdataloader.iex.IexBatchConfiguration;
import com.josecponce.stockdata.iexdataloader.iex.iextrading.IexClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import pl.zankowski.iextrading4j.api.stocks.ChartRange;

import java.util.*;

@Component
@Slf4j
public class ScheduledBatchJobs {
    private final ApplicationContext context;
    private final JobLauncher jobLauncher;
    private final JobParametersBuilder parameters;
    private final IexClient client;

    private Set<String> runningJobs = new HashSet<>();

    public ScheduledBatchJobs(ApplicationContext context, JobLauncher jobLauncher, JobParametersBuilder parameters, IexClient client) {
        this.context = context;
        this.jobLauncher = jobLauncher;
        this.parameters = parameters;
        this.client = client;
    }

//    @Scheduled(fixedDelayString = "${fixedDelayMs}")
    public void iexJob() {
        try {
            runJob(IexBatchConfiguration.LOAD_IEX_DATA_JOB, "IEX job execution finished in {}s with exit status {}");
        } finally {
            client.setRange(ChartRange.ONE_MONTH);//after letting it iexJob once
        }
    }

//    @Scheduled(fixedDelayString = "${fixedDelayMs}")
    public void treasuryJob() {
        runJob(GovBatchConfiguration.LOAD_TREASURY_DATA_JOB, "Treasury yields job execution finished in {}s with exit status {}");
    }

    @Scheduled(fixedDelayString = "${fixedDelayMs}")
    public void alphaVantageJob() {
        runJob(AlphaVantageBatchConfiguration.LOAD_ALPHA_VANTAGE_STOCK_DATA_JOB, "Alpha Vantage job execution finished in {}s with exit status {}");
    }

    private void runJob(String jobBeanName, String logFormat) {
        synchronized (this) {
            if (runningJobs.contains(jobBeanName)) {
                return;//this job is already running
            } else {
                runningJobs.add(jobBeanName);
            }
        }
        Job job = context.getBean(jobBeanName, Job.class);
        long time = System.currentTimeMillis();
        try {
            JobExecution execution = jobLauncher.run(job, parameters.getNextJobParameters(job).toJobParameters());
            log.info(logFormat, (System.currentTimeMillis() - time) / 1000, execution.getExitStatus());
        } catch (JobExecutionAlreadyRunningException | JobRestartException | JobInstanceAlreadyCompleteException | JobParametersInvalidException e) {
            log.error(String.format("Unexpected error when attempting to run batch job '%s'", jobBeanName), e);
        }
        synchronized (this) {
            runningJobs.remove(jobBeanName);
        }
    }
}
