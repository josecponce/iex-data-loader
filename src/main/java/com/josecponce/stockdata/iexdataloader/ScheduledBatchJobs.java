package com.josecponce.stockdata.iexdataloader;

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

@Component
@Slf4j
public class ScheduledBatchJobs {
    private final ApplicationContext context;
    private final JobLauncher jobLauncher;
    private final JobParametersBuilder parameters;
    private final IexClient client;

    public ScheduledBatchJobs(ApplicationContext context, JobLauncher jobLauncher, JobParametersBuilder parameters, IexClient client) {
        this.context = context;
        this.jobLauncher = jobLauncher;
        this.parameters = parameters;
        this.client = client;
    }

//    @Scheduled(fixedDelayString = "${fixedDelayMs}")
    public void iexJob() throws JobParametersInvalidException, JobExecutionAlreadyRunningException, JobRestartException, JobInstanceAlreadyCompleteException {
        try {
            Job job = context.getBean(IexBatchConfiguration.LOAD_IEX_DATA_JOB, Job.class);
            long time = System.currentTimeMillis();
            JobExecution execution = jobLauncher.run(job, parameters.getNextJobParameters(job).toJobParameters());
            log.info("IEX job execution finished in {}s with exit status {}", (System.currentTimeMillis() - time) / 1000, execution.getExitStatus());
        } finally {
            client.setRange(ChartRange.ONE_MONTH);//after letting it iexJob once
        }
    }

    @Scheduled(fixedDelayString = "${fixedDelayMs}")
    public void treasuryJob()  throws JobParametersInvalidException, JobExecutionAlreadyRunningException, JobRestartException, JobInstanceAlreadyCompleteException {
        Job job = context.getBean(GovBatchConfiguration.LOAD_TREASURY_DATA_JOB, Job.class);
        long time = System.currentTimeMillis();
        JobExecution execution = jobLauncher.run(job, parameters.getNextJobParameters(job).toJobParameters());
        log.info("Treasury yields job execution finished in {}s with exit status {}", (System.currentTimeMillis() - time) / 1000, execution.getExitStatus());
    }
}
