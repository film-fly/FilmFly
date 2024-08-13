package com.sparta.filmfly.global.config;

import com.sparta.filmfly.global.common.batch.JobCompletionListener;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@EnableBatchProcessing
public class BatchConfig {

    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;

    public BatchConfig(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        this.jobRepository = jobRepository;
        this.transactionManager = transactionManager;
    }

    @Bean
    public Job movieCrawlingJob(Step movieCrawlingStep, JobExecutionListener jobExecutionListener) {
        return new JobBuilder("movieCrawlingJob", jobRepository)
                .incrementer(new RunIdIncrementer())
                .listener(jobExecutionListener)
                .start(movieCrawlingStep)
                .build();
    }

    @Bean
    public Step movieCrawlingStep(Tasklet movieCrawlingTasklet) {
        return new StepBuilder("movieCrawlingStep", jobRepository)
                .tasklet(movieCrawlingTasklet, transactionManager)
                .build();
    }

    @Bean
    public Job movieSearchJob(Step movieSearchStep, JobExecutionListener jobExecutionListener) {
        return new JobBuilder("movieSearchJob", jobRepository)
                .incrementer(new RunIdIncrementer())
                .listener(jobExecutionListener)
                .start(movieSearchStep)
                .build();
    }

    @Bean
    public Step movieSearchStep(Tasklet movieSearchTasklet) {
        return new StepBuilder("movieSearchStep", jobRepository)
                .tasklet(movieSearchTasklet, transactionManager)
                .build();
    }

    @Bean
    public JobExecutionListener jobExecutionListener() {
        return new JobCompletionListener();
    }
}