package com.sparta.filmfly.domain.file.service;

import com.sparta.filmfly.domain.file.util.FileUtils;
import java.io.File;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Service
@RequiredArgsConstructor
public class FileService {

    private final FileUtils fileUtils;

    public String saveFileToLocal(MultipartFile file) {
        String originalFilename = file.getOriginalFilename();
        String folderLocation = fileUtils.getAbsoluteUploadFolder();
        String uuidFileName = fileUtils.createUuidFileName(originalFilename);
        String fileFullPath = folderLocation + uuidFileName;
        File saveFile = new File(fileFullPath);
        try {
            file.transferTo(saveFile);
        } catch (IOException e) {
            log.error("local에 파일을 저장할 수 없습니다. (원본 파일 이름 : {})", originalFilename, e);
            return null;
        }
        return "/temp/" + uuidFileName;
    }
}