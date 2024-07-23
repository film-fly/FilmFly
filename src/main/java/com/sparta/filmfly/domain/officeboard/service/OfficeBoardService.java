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
// 공통 -> 로그인 완료되면 유저 추가
public class OfficeBoardService {

    private final OfficeBoardRepository officeBoardRepository;
    private final MediaService mediaService;

    @Transactional
    public OfficeBoardResponseDto createOfficeBoard(OfficeBoard officeBoard,
            List<MultipartFile> files) {

        officeBoard.validUser();
        OfficeBoard savedOfficeBoard = officeBoardRepository.save(officeBoard);

        OfficeBoardResponseDto officeBoardresponseDto = OfficeBoardResponseDto.fromEntity(
                officeBoard);

        if (!officeBoard.isFilesNotNull(files)) {
            return officeBoardresponseDto;
        }

        for (MultipartFile file : files) {
            MediaResponseDto mediaResponseDto = mediaService.saveMedia(MediaTypeEnum.OFFICE_BOARD,
                    savedOfficeBoard.getId(), file);
            officeBoardresponseDto.addMediaDto(mediaResponseDto);
        }
        return officeBoardresponseDto;
    }

    @Transactional
    public List<OfficeBoardResponseDto> findAll(Pageable pageable) {

        List<OfficeBoardResponseDto> list = officeBoardRepository.findAllByOrderByCreatedAtDesc(
                        pageable)
                .stream()
                .map(OfficeBoardResponseDto::fromEntity)
                .toList();

        for (OfficeBoardResponseDto responseDto : list) {
            OfficeBoard officeBoard = officeBoardRepository.findByIdOrElseThrow(
                    responseDto.getId());

            List<Media> mediaList = mediaService.getMediaList(MediaTypeEnum.OFFICE_BOARD,
                    officeBoard.getId());

            for (Media media : mediaList) {
                responseDto.addMediaDto(MediaResponseDto.fromEntity(media));
            }
        }

        return list;
    }

    @Transactional
    public OfficeBoardResponseDto findOne(Long id) {

        OfficeBoard officeBoard = officeBoardRepository.findByIdOrElseThrow(id);
        List<Media> mediaList = mediaService.getMediaList(MediaTypeEnum.OFFICE_BOARD,
                officeBoard.getId());

        OfficeBoardResponseDto officeResponseDto = OfficeBoardResponseDto.fromEntity(officeBoard);

        for (Media media : mediaList) {
            officeResponseDto.addMediaDto(MediaResponseDto.fromEntity(media));
        }

        return officeResponseDto;

    }

    @Transactional
    public OfficeBoardResponseDto update(User user, Long id,
            OfficeBoardRequestDto requestDto, List<MultipartFile> files) {

        OfficeBoard officeBoard = officeBoardRepository.findByIdOrElseThrow(id);
        officeBoard.validUser();
        officeBoard.checkOwnerUser(user);

        OfficeBoardResponseDto responseDto = OfficeBoardResponseDto.fromEntity(officeBoard);
        mediaService.deleteAllMedia(MediaTypeEnum.OFFICE_BOARD, officeBoard.getId());
        officeBoard.update(requestDto);

        if (!officeBoard.isFilesNotNull(files)) {
            return responseDto;
        }

        for (MultipartFile file : files) {
            MediaResponseDto mediaResponseDto = mediaService.saveMedia(MediaTypeEnum.OFFICE_BOARD,
                    officeBoard.getId(), file);
            responseDto.addMediaDto(mediaResponseDto);
        }

        return responseDto;

    }


    @Transactional
    public OfficeBoardResponseDto delete(User user, Long id) {

        OfficeBoard officeBoard = officeBoardRepository.findByIdOrElseThrow(id);

        officeBoard.validUser();
        officeBoard.checkOwnerUser(user);
        officeBoard.delete();
        return OfficeBoardResponseDto.fromEntity(officeBoard);
    }

}