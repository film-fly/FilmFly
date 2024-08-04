package com.sparta.filmfly.domain.officeboard.service;

import com.sparta.filmfly.domain.file.service.FileService;
import com.sparta.filmfly.domain.media.entity.MediaTypeEnum;
import com.sparta.filmfly.domain.officeboard.dto.OfficeBoardPageResponseDto;
import com.sparta.filmfly.domain.officeboard.dto.OfficeBoardRequestDto;
import com.sparta.filmfly.domain.officeboard.dto.OfficeBoardResponseDto;
import com.sparta.filmfly.domain.officeboard.dto.OfficeBoardUpdateResponseDto;
import com.sparta.filmfly.domain.officeboard.entity.OfficeBoard;
import com.sparta.filmfly.domain.officeboard.repository.OfficeBoardRepository;
import com.sparta.filmfly.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor

public class OfficeBoardService {

    private final OfficeBoardRepository officeBoardRepository;
    private final FileService fileService;

    /**
     * 운영보드 생성
     */
    @Transactional
    public OfficeBoardResponseDto createOfficeBoard(User user, OfficeBoardRequestDto requestDto) {
        OfficeBoard entity = requestDto.toEntity(user);
        //entity.checkAdmin(); // 관리자인지 확인하는거 여기 있어야하나? 시큐리티에서 막아주던가
        OfficeBoard savedOfficeBoard = officeBoardRepository.save(entity); // 일단 저장 후 Id를 생성

        String content = requestDto.getContent();
        String modifiedContent = fileService.uploadLocalImageToS3(MediaTypeEnum.OFFICE_BOARD,savedOfficeBoard.getId(), content);

        savedOfficeBoard.updateTitleContent(null, modifiedContent);
        OfficeBoard updatedOfficeBoard = officeBoardRepository.save(savedOfficeBoard);

        return OfficeBoardResponseDto.fromEntity(updatedOfficeBoard);
    }

    /**
     * 운영보드 단일 조회
     */
    @Transactional
    public OfficeBoardResponseDto getOfficeBoard(Long id) {
        OfficeBoard officeBoard = officeBoardRepository.findByIdOrElseThrow(id);

        officeBoard.addHits();
        OfficeBoard savedofficeBoard = officeBoardRepository.save(officeBoard);

        return OfficeBoardResponseDto.fromEntity(savedofficeBoard);
    }

    /**
     * 운영보드 전체 조회(페이징 포함)
     */
    @Transactional(readOnly = true)
    public OfficeBoardPageResponseDto getPageOfficeBoards(Integer pageNum, Integer size) {
        Pageable pageable = PageRequest.of(pageNum-1, size, Sort.by(Sort.Direction.DESC, "createdAt"));

        Page<OfficeBoard> officeBoardPage = officeBoardRepository.findAllByOrderByCreatedAtDesc(pageable);

        return OfficeBoardPageResponseDto.builder()
                .totalPages(officeBoardPage.getTotalPages())
                .totalElements(officeBoardPage.getTotalElements())
                .currentPage(officeBoardPage.getNumber() + 1)
                .pageSize(officeBoardPage.getSize())
                .content(officeBoardPage.stream().map(OfficeBoardResponseDto::fromEntity).toList())
                .build();
    }

    /**
     * 운영보드 수정 페이지 정보
     */
    public OfficeBoardUpdateResponseDto forUpdateOfficeBoard(User user, Long boardId) {
        OfficeBoard officeBoard = officeBoardRepository.findByIdOrElseThrow(boardId);
        //officeBoard.checkOwnerUser(user);  관리자는 서로 편집 가능하게?
        return OfficeBoardUpdateResponseDto.fromEntity(officeBoard);
    }

    /**
     * 운영보드 수정
     */
    @Transactional
    public OfficeBoardResponseDto updateOfficeBoard(User user, OfficeBoardRequestDto requestDto, Long boardId) {
        OfficeBoard officeBoard = officeBoardRepository.findByIdOrElseThrow(boardId);
        //officeBoard.checkOwnerUser(user);

        String content = requestDto.getContent();
        fileService.checkModifiedImageFile(MediaTypeEnum.OFFICE_BOARD, officeBoard.getId(), content); //이미지 변경 확인
        String modifiedContent = fileService.uploadLocalImageToS3(MediaTypeEnum.OFFICE_BOARD,officeBoard.getId(),content); //이미지 S3 변환

        officeBoard.updateTitleContent(requestDto.getTitle(),modifiedContent);
        OfficeBoard updatedOfficeBoard = officeBoardRepository.save(officeBoard);

        return OfficeBoardResponseDto.fromEntity(updatedOfficeBoard);
    }

    /**
     * 운영보드 삭제
     */
    @Transactional
    public String deleteOfficeBoard(User user, Long id) {
        OfficeBoard officeBoard = officeBoardRepository.findByIdOrElseThrow(id);

        officeBoard.checkOwnerUser(user);
        officeBoard.deleteOfficeBoard(); //officeBoardRepository.delete 대신 직접 deleteAt에 값을 넣어줌, 참고용으로 일단 놔둠
        return "공지사항이 삭제되었습니다.";
    }
}