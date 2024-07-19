package com.sparta.filmfly;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SpringInitTemplateApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringInitTemplateApplication.class, args);
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