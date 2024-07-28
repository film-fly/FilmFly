package com.sparta.filmfly.domain.officeboard.controller;

import com.sparta.filmfly.domain.officeboard.dto.OfficeBoardRequestDto;
import com.sparta.filmfly.domain.officeboard.dto.OfficeBoardResponseDto;
import com.sparta.filmfly.domain.officeboard.entity.OfficeBoard;
import com.sparta.filmfly.domain.officeboard.service.OfficeBoardService;
import com.sparta.filmfly.global.auth.UserDetailsImpl;
import com.sparta.filmfly.global.common.response.DataResponseDto;
import com.sparta.filmfly.global.common.response.ResponseUtils;
import com.sparta.filmfly.global.util.PageUtils;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/officeboard")

// 공통 로그인 구현 완료되면 유저검증 추가
public class OfficeBoardController {

    private final OfficeBoardService officeBoardService;

    /**
     * 운영보드 생성
     */
    @PostMapping
    public ResponseEntity<DataResponseDto<OfficeBoardResponseDto>> createOfficeBoard(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @Valid @RequestPart(value = "officeBoardRequestDto") OfficeBoardRequestDto requestDto,
            @RequestPart(value = "files", required = false) List<MultipartFile> files

    ) {
        OfficeBoard officeBoard = requestDto.toEntity(userDetails.getUser(), requestDto);

        OfficeBoardResponseDto responseDto = officeBoardService.createOfficeBoard(officeBoard,
                files);
        return ResponseUtils.success(responseDto);
    }

    /**
     * 운영보드 전체 조회
     */
    @GetMapping
    public ResponseEntity<DataResponseDto<List<OfficeBoardResponseDto>>> getAllOfficeBoards(
            @RequestParam(required = false, defaultValue = "1") int page,
            @RequestParam(required = false, defaultValue = "5") int size,
            @RequestParam(required = false, defaultValue = "createdAt") String sortBy,
            @RequestParam(required = false, defaultValue = "false") boolean isAsc
    ) {

        Pageable pageable = PageUtils.of(page, size, sortBy, isAsc);
        List<OfficeBoardResponseDto> responseDto = officeBoardService.getAllOfficeBoards(pageable);
        return ResponseUtils.success(responseDto);
    }

    /**
     * 운영보드 단일 조회
     */
    @GetMapping("/{boardId}")
    public ResponseEntity<DataResponseDto<OfficeBoardResponseDto>> getOfficeBoard(
            @PathVariable Long boardId
    ) {
        OfficeBoardResponseDto responseDto = officeBoardService.getOfficeBoard(boardId);
        return ResponseUtils.success(responseDto);
    }

    /**
     * 운영보드 수정
     */
    @PatchMapping("/{boardId}")
    public ResponseEntity<DataResponseDto<OfficeBoardResponseDto>> updateOfficeBoard(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @Valid @RequestPart(value = "officeBoardRequestDto") OfficeBoardRequestDto requestDto,
            @RequestPart(value = "files", required = false) List<MultipartFile> files,
            @PathVariable Long boardId
    ) {
        OfficeBoardResponseDto responseDto = officeBoardService.updateOfficeBoard(
                userDetails.getUser(),
                boardId, requestDto, files);
        return ResponseUtils.success(responseDto);
    }

    /**
     * 운영보드 삭제
     */
    @DeleteMapping("/{boardId}")
    public ResponseEntity<DataResponseDto<OfficeBoardResponseDto>> deleteOfficeBoard(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @PathVariable Long boardId
    ) {
        OfficeBoardResponseDto responseDto = officeBoardService.deleteOfficeBoard(
                userDetails.getUser(),
                boardId);
        return ResponseUtils.success(responseDto);
    }

}