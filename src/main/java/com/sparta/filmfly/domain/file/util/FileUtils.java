package com.sparta.filmfly.domain.file.util;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class FileUtils {

    private final String uploadLocation = "/src/main/resources/static/temp/";

    public String getAbsoluteUploadFolder() {
        try {
            File file = new File("");
            String currentAbsolutePath = file.getAbsolutePath() + uploadLocation;
            Path path = Paths.get(currentAbsolutePath);
            if (!Files.exists(path)) {
                Files.createDirectories(path);
            }
            return currentAbsolutePath;
        } catch (IOException e) {
            throw new RuntimeException("사진을 업로드할 폴더를 생성할 수 없습니다.", e);
        }
    }

    public String createUuidFileName(String originalFileName) {
        String extension = extractExtension(originalFileName);
        return UUID.randomUUID() + "." + extension;
    }

    public String extractOriginalName(String originalFileName) {
        return originalFileName.substring(0, originalFileName.indexOf("."));
    }

    public String extractExtension(String originalFileName) {
        int point = originalFileName.lastIndexOf(".");
        return originalFileName.substring(point + 1);
    }
}