package com.sparta.filmfly.domain.officeboard.controller;

import com.sparta.filmfly.domain.officeboard.dto.OfficeBoardRequestDto;
import com.sparta.filmfly.domain.officeboard.dto.OfficeBoardResponseDto;
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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/officeboard")

// 공통 로그인 구현 완료되면 유저검증 추가
public class OfficeBoardController {

    private final OfficeBoardService officeBoardService;

    @PostMapping
    public ResponseEntity<DataResponseDto<OfficeBoardResponseDto>> createOfficeBoard(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @Valid @RequestBody OfficeBoardRequestDto requestDto
    ) {
        OfficeBoardResponseDto responseDto = officeBoardService.createOfficeBoard(
                userDetails.getUser(), requestDto);
        return ResponseUtils.success(responseDto);
    }

    @GetMapping
    public ResponseEntity<DataResponseDto<List<OfficeBoardResponseDto>>> findAll(
            @RequestParam(required = false, defaultValue = "1") int page,
            @RequestParam(required = false, defaultValue = "5") int size,
            @RequestParam(required = false, defaultValue = "createdAt") String sortBy,
            @RequestParam(required = false, defaultValue = "false") boolean isAsc
    ) {

        Pageable pageable = PageUtils.of(page, size, sortBy, isAsc);
        List<OfficeBoardResponseDto> responseDto = officeBoardService.findAll(pageable);
        return ResponseUtils.success(responseDto);
    }

    @GetMapping("/{boardId}")
    public ResponseEntity<DataResponseDto<OfficeBoardResponseDto>> findOne(
            @PathVariable Long boardId
    ) {
        System.out.println("============================");
        System.out.println("boardId = " + boardId);
        OfficeBoardResponseDto responseDto = officeBoardService.findOne(boardId);
        return ResponseUtils.success(responseDto);
    }

    @PatchMapping("/{boardId}")
    public ResponseEntity<DataResponseDto<OfficeBoardResponseDto>> update(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @PathVariable Long boardId,
            @Valid @RequestBody OfficeBoardRequestDto requestDto
    ) {
        OfficeBoardResponseDto responseDto = officeBoardService.update(userDetails.getUser(), boardId,
                requestDto);
        return ResponseUtils.success(responseDto);
    }

    @DeleteMapping("/{boardId}")
    public ResponseEntity<DataResponseDto<OfficeBoardResponseDto>> delete(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @PathVariable Long boardId
    ) {
        OfficeBoardResponseDto responseDto = officeBoardService.delete(userDetails.getUser(), boardId);
        return ResponseUtils.success(responseDto);
    }

}