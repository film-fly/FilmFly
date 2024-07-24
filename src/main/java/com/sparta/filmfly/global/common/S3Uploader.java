package com.sparta.filmfly.global.common;


import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.sparta.filmfly.domain.media.entity.MediaTypeEnum;
import com.sparta.filmfly.global.common.response.ResponseCodeEnum;
import com.sparta.filmfly.global.exception.custom.detail.UploadException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Component
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

        amazonS3.putObject(bucketName, fileName, file.getInputStream(), metadata);
        return amazonS3.getUrl(bucketName, fileName).toString();
    }

    //보드 파일 업로드
    public String boardFileUpload(MediaTypeEnum mediaType, Long typeId, MultipartFile file) throws IOException {
        StringBuilder sb = new StringBuilder();
        if(mediaType == MediaTypeEnum.BOARD)
            sb.append("board/");
        else if(mediaType == MediaTypeEnum.OFFICE_BOARD)
            sb.append("officeBoard/");
        else
            throw new UploadException(ResponseCodeEnum.UPLOAD_FAILED);
        sb.append(typeId).append("/").append(file.getOriginalFilename());
        String fileName = sb.toString(); //같은 이름의 파일은 그 위에 다시 업로드됨

        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType(file.getContentType());
        metadata.setContentLength(file.getSize());

        amazonS3.putObject(bucketName, fileName, file.getInputStream(), metadata); // 파일 업로드
        return amazonS3.getUrl(bucketName, fileName).toString();
    }

    public void boardFileDelete(Long typeId, String fileName){
        String key = "board/" + typeId + "/" + fileName; //s3에 올라간 파일 이름
        try{
            amazonS3.deleteObject(new DeleteObjectRequest(bucketName, key));
        }catch (Exception e){
            throw new UploadException(ResponseCodeEnum.UPLOAD_FAILED);
        }
    }
}
