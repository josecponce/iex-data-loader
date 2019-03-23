package com.josecponce.stockdata.iexdataloader.scheduled;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ScheduledBatchJobs {
    private final ObjectFactory<Job> jobFactory;
    private final JobLauncher jobLauncher;
    private final JobParametersBuilder parameters;

    public ScheduledBatchJobs(ObjectFactory<Job> jobFactory, JobLauncher jobLauncher, JobParametersBuilder parameters) {
        this.jobFactory = jobFactory;
        this.jobLauncher = jobLauncher;
        this.parameters = parameters;
    }

    @Scheduled(fixedDelayString = "${fixedDelayMs}")
    public void run() throws JobParametersInvalidException, JobExecutionAlreadyRunningException, JobRestartException, JobInstanceAlreadyCompleteException {
        Job job = jobFactory.getObject();
        long time = System.currentTimeMillis();
        JobExecution execution = jobLauncher.run(job, parameters.getNextJobParameters(job).toJobParameters());
        log.info("Execution finished in {}s with exit status {}", (System.currentTimeMillis() - time) / 1000, execution.getExitStatus());
    }
}
