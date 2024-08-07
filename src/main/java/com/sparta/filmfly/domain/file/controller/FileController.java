package com.sparta.filmfly.domain.file.controller;

import com.sparta.filmfly.domain.file.service.FileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RestController
@RequiredArgsConstructor
public class FileController {

    private final FileService fileService;

    @PostMapping("/image/upload")
    public String uploadImage(@RequestParam MultipartFile file) {
        String fileName = fileService.saveFileToLocal(file);

        return fileName;
    }

    @DeleteMapping("/image/delete")
    public String deleteImage(@RequestParam String imageName) {
        fileService.deleteFileToLocal(imageName);

        return "삭제 완료";
    }
}