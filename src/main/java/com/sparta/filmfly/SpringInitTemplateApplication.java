package com.sparta.filmfly;

import jakarta.annotation.PostConstruct;
import java.util.TimeZone;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@EnableBatchProcessing
public class SpringInitTemplateApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringInitTemplateApplication.class, args);
    }

    @PostConstruct
    void started(){
        TimeZone.setDefault(TimeZone.getTimeZone("Asia/Seoul"));
        System.out.println("현재 타임존: " + TimeZone.getDefault().getID());
    }

//    @Component
//    @RequiredArgsConstructor
//    public static class DataLoader implements CommandLineRunner {
//
//        private final JdbcTemplateBatchRepository repository;
//
//        @Override
//        public void run(String... args) throws Exception {
//            repository.batchInsertCards();
//        }
//    }
}