package com.sparta.filmfly.dummytest;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;

@Component
@RequiredArgsConstructor
public class DataLoader implements CommandLineRunner {
    private final DataSource dataSource;

    @Override
    public void run(String... args) throws Exception {
        executeSqlScript("dummy/userData.sql");
        executeSqlScript("dummy/boardData.sql");
        executeSqlScript("dummy/commentData.sql");
        executeSqlScript("dummy/movieData.sql");
        executeSqlScript("dummy/reviewData.sql");
        executeSqlScript("dummy/reactionData.sql");
    }

    private void executeSqlScript(String filePath) throws Exception {
        ResourceDatabasePopulator resourceDatabasePopulator = new ResourceDatabasePopulator(
                false, // continueOnError
                false, // ignoreFailedDrops
                "UTF-8", // encoding
                new ClassPathResource(filePath)
        );
        try {
            resourceDatabasePopulator.execute(dataSource);
        } catch (Exception e) {
            System.err.println("Error executing script: " + filePath);
            e.printStackTrace();
        }
    }
}
