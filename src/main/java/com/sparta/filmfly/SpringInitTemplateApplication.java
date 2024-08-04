package com.sparta.filmfly;

import org.apache.catalina.Context;
import org.apache.catalina.connector.Connector;
import org.apache.tomcat.util.descriptor.web.SecurityCollection;
import org.apache.tomcat.util.descriptor.web.SecurityConstraint;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.servlet.server.ServletWebServerFactory;
import org.springframework.context.annotation.Bean;

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