package com.sparta.filmfly.global.common.batch.hardDelete;

import lombok.AllArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;


@Component
@EnableScheduling
@AllArgsConstructor
public class HardDeleteScheduler {

    private final JobLauncher jobLauncher;
    private final Job hardDeleteJob;

    @Scheduled(cron = "0 30 4 * * ?")
    public void runHardDeleteJob() {
        try {
            jobLauncher.run(hardDeleteJob, new JobParametersBuilder()
                    .addLong("timestamp", System.currentTimeMillis())
                    .toJobParameters());
        } catch (Exception e) {
            e.getStackTrace();
        }
    }
}
