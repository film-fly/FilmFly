package com.sparta.filmfly.global.common.batch;

import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.stereotype.Component;

@Component
public class JobCompletionListener implements JobExecutionListener {

    @Override
    public void beforeJob(JobExecution jobExecution) {
        System.out.println("Batch job is starting...");
    }

    @Override
    public void afterJob(JobExecution jobExecution) {
        System.out.println("Batch job has finished.");
    }
}