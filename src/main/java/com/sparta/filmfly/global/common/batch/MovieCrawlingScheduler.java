package com.sparta.filmfly.global.common.batch;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@EnableScheduling
public class MovieCrawlingScheduler {

    private final JobLauncher jobLauncher;
    private final Job movieCrawlingJob;
    private final Job movieSearchJob;

    public MovieCrawlingScheduler(JobLauncher jobLauncher, Job movieCrawlingJob, Job movieSearchJob) {
        this.jobLauncher = jobLauncher;
        this.movieCrawlingJob = movieCrawlingJob;
        this.movieSearchJob = movieSearchJob;
    }

    @Scheduled(cron = "0 0 4 * * ?")
    public void runMovieCrawlingJob() {
        try {
            jobLauncher.run(movieCrawlingJob, new JobParametersBuilder()
                    .addLong("timestamp", System.currentTimeMillis())
                    .toJobParameters());
        } catch (Exception e) {
            e.getStackTrace();
        }
    }

//    @Scheduled(cron = "0 0 5 * * ?")
//    public void runSearchJob() {
//        try {
//            jobLauncher.run(movieSearchJob, new JobParametersBuilder()
//                    .addLong("timestamp", System.currentTimeMillis())
//                    .toJobParameters());
//        } catch (Exception e) {
//            e.getStackTrace();
//        }
//    }
}