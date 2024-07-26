package com.sparta.filmfly.domain.media.service;

import com.sparta.filmfly.domain.media.dto.MediaResponseDto;
import com.sparta.filmfly.domain.media.entity.Media;
import com.sparta.filmfly.domain.media.entity.MediaTypeEnum;
import com.sparta.filmfly.domain.media.repository.MediaRepository;
import com.sparta.filmfly.global.common.S3Uploader;
import com.sparta.filmfly.global.common.response.ResponseCodeEnum;
import com.sparta.filmfly.global.exception.custom.detail.UploadException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class MediaService {
    private final MediaRepository mediaRepository;
    private final S3Uploader s3Uploader;

    /**
     * 미디어 생성
     * 파일 하나씩 저장 후 Dto 반환
     */
    @Transactional
    public MediaResponseDto createMedia(MediaTypeEnum mediaType, Long typeId, MultipartFile file) {
        MediaResponseDto mediaResponseDto = null;

        boolean trySuccess = false;
        String mediaUrl = null;
        try {
            mediaUrl = s3Uploader.boardFileUpload(mediaType,typeId,file);
            trySuccess = true;
        } catch (IOException e) {
            throw new UploadException(ResponseCodeEnum.UPLOAD_FAILED);
        }
        finally {
            if (trySuccess) {
                Media media = Media.builder()
                        .s3Url(mediaUrl)
                        .fileName(file.getOriginalFilename())
                        .type(mediaType)
                        .typeId(typeId)
                        .build();
                Media savedMedia = mediaRepository.save(media);
                mediaResponseDto = MediaResponseDto.fromEntity(savedMedia);
            }
        }
        return mediaResponseDto;
    }

    /**
     * 미디어 리스트 조회
     */
    @Transactional(readOnly = true)
    public List<Media> getListMedia(MediaTypeEnum mediaType, Long typeId) {
        return mediaRepository.findAllByTypeAndTypeId(mediaType,typeId);
    }

    /**
     * 해당 게시글 모든 미디어 삭제
     */
    @Transactional
    public void deleteAllMedia(MediaTypeEnum mediaType, Long typeId) {
        List<Media> mediaList = getListMedia(mediaType,typeId);
        for (Media media : mediaList) {
            s3Uploader.boardFileDelete(mediaType,media.getTypeId(),media.getFileName());
            mediaRepository.delete(media);
        }
    }
}