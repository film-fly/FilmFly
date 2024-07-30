package com.sparta.filmfly.global.common;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.UUID;

@Component
@Slf4j
public class S3Uploader {

    private final AmazonS3 amazonS3;
    private final String bucketName;

    @Autowired
    public S3Uploader(AmazonS3 amazonS3, @Value("${cloud.aws.s3.bucket}") String bucketName) {
        this.amazonS3 = amazonS3;
        this.bucketName = bucketName;
    }

    public String upload(MultipartFile file, String dirName) throws IOException {
        String fileName = dirName + "/" + UUID.randomUUID() + "_" + file.getOriginalFilename();
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType(file.getContentType());
        metadata.setContentLength(file.getSize());

        log.info("Uploading file: {}", fileName);
        amazonS3.putObject(bucketName, fileName, file.getInputStream(), metadata);
        return amazonS3.getUrl(bucketName, fileName).toString();
    }

    public void delete(String fileUrl) {
        if (fileUrl == null || fileUrl.isEmpty()) {
            return;
        }
        String fileName = fileUrl.substring(fileUrl.indexOf(".com/") + 5);
        log.info("Deleting file: {}", fileName);
        amazonS3.deleteObject(new DeleteObjectRequest(bucketName, fileName));
    }

    public boolean isFileSame(MultipartFile file, String fileUrl) throws IOException {
        if (fileUrl == null || fileUrl.isEmpty()) {
            return false;
        }

        String fileName = fileUrl.substring(fileUrl.indexOf(".com/") + 5);
        try {
            ObjectMetadata metadata = amazonS3.getObjectMetadata(bucketName, fileName);
            boolean isSame = metadata.getContentLength() == file.getSize() &&
                    metadata.getContentType().equals(file.getContentType());
            log.info("Comparing files - is same: {}", isSame);
            return isSame;
        } catch (AmazonS3Exception e) {
            if (e.getStatusCode() == 404) {
                // 파일이 존재하지 않으면 false 반환
                log.warn("File not found: {}", fileName);
                return false;
            } else {
                throw e; // 다른 예외는 다시 던짐
            }
        }
    }
}
