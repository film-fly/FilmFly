package com.sparta.filmfly.domain.officeboard.service;

import com.sparta.filmfly.domain.media.dto.MediaResponseDto;
import com.sparta.filmfly.domain.media.entity.Media;
import com.sparta.filmfly.domain.media.entity.MediaTypeEnum;
import com.sparta.filmfly.domain.media.service.MediaService;
import com.sparta.filmfly.domain.officeboard.dto.OfficeBoardRequestDto;
import com.sparta.filmfly.domain.officeboard.dto.OfficeBoardResponseDto;
import com.sparta.filmfly.domain.officeboard.entity.OfficeBoard;
import com.sparta.filmfly.domain.officeboard.repository.OfficeBoardRepository;
import com.sparta.filmfly.domain.user.entity.User;
import com.sparta.filmfly.global.util.FileUtils;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Service
@RequiredArgsConstructor

public class OfficeBoardService {

    private final OfficeBoardRepository officeBoardRepository;
    private final MediaService mediaService;

    /**
     * 운영보드 생성
     */
    @Transactional
    public OfficeBoardResponseDto createOfficeBoard(OfficeBoard officeBoard,
            List<MultipartFile> files) {

        officeBoard.checkAdmin();
        OfficeBoard savedOfficeBoard = officeBoardRepository.save(officeBoard);

        OfficeBoardResponseDto responseDto = OfficeBoardResponseDto.fromEntity(
                officeBoard);

        if (!FileUtils.isEmpty(files)) {
            // responseDto에 요청 file들 추가
            for (MultipartFile file : files) {
                MediaResponseDto mediaResponseDto = mediaService.createMedia(
                        MediaTypeEnum.OFFICE_BOARD,
                        savedOfficeBoard.getId(), file);
                responseDto.addMediaDto(mediaResponseDto);
            }
        }

        return responseDto;
    }

    /**
     * 운영보드 전체 조회(페이징 포함)
     */
    @Transactional(readOnly = true)
    public List<OfficeBoardResponseDto> getAllOfficeBoards(Pageable pageable) {

        List<OfficeBoardResponseDto> list = officeBoardRepository.findAllByOrderByCreatedAtDesc(
                        pageable)
                .stream()
                .map(OfficeBoardResponseDto::fromEntity)
                .toList();

        for (OfficeBoardResponseDto responseDto : list) {
            OfficeBoard officeBoard = officeBoardRepository.findByIdOrElseThrow(
                    responseDto.getId());

            List<Media> mediaList = mediaService.getListMedia(MediaTypeEnum.OFFICE_BOARD,
                    officeBoard.getId());

            for (Media media : mediaList) {
                responseDto.addMediaDto(MediaResponseDto.fromEntity(media));
            }
        }

        return list;
    }

    /**
     * 운영보드 단일 조회
     */
    @Transactional(readOnly = true)
    public OfficeBoardResponseDto getOfficeBoard(Long id) {

        OfficeBoard officeBoard = officeBoardRepository.findByIdOrElseThrow(id);
        List<Media> mediaList = mediaService.getListMedia(MediaTypeEnum.OFFICE_BOARD,
                officeBoard.getId());

        OfficeBoardResponseDto officeResponseDto = OfficeBoardResponseDto.fromEntity(officeBoard);

        for (Media media : mediaList) {
            officeResponseDto.addMediaDto(MediaResponseDto.fromEntity(media));
        }

        return officeResponseDto;

    }

    /**
     * 운영보드 수정
     */
    @Transactional
    public OfficeBoardResponseDto updateOfficeBoard(User user, Long id,
            OfficeBoardRequestDto requestDto, List<MultipartFile> files) {

        OfficeBoard officeBoard = officeBoardRepository.findByIdOrElseThrow(id);
        officeBoard.checkAdmin();
        officeBoard.checkOwnerUser(user);

        OfficeBoardResponseDto responseDto = OfficeBoardResponseDto.fromEntity(officeBoard);
        mediaService.deleteAllMedia(MediaTypeEnum.OFFICE_BOARD, officeBoard.getId());
        officeBoard.updateOfficeBoard(requestDto);

        if (!FileUtils.isEmpty(files)) {
            for (MultipartFile file : files) {
                MediaResponseDto mediaResponseDto = mediaService.createMedia(
                        MediaTypeEnum.OFFICE_BOARD,
                        officeBoard.getId(), file);
                responseDto.addMediaDto(mediaResponseDto);
            }
        }

        return responseDto;

    }

    /**
     * 운영보드 삭제
     */
    @Transactional
    public OfficeBoardResponseDto deleteOfficeBoard(User user, Long id) {

        OfficeBoard officeBoard = officeBoardRepository.findByIdOrElseThrow(id);

        officeBoard.checkAdmin();
        officeBoard.checkOwnerUser(user);
        officeBoardRepository.delete(officeBoard);
        return OfficeBoardResponseDto.fromEntity(officeBoard);
    }

}