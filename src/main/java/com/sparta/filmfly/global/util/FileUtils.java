package com.sparta.filmfly.global.util;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public abstract class FileUtils {
    public static Boolean isEmpty(List<MultipartFile> files) {
        return files == null || files.isEmpty() || files.get(0).isEmpty();
    }
}
