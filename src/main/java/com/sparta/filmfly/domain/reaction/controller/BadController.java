package com.sparta.filmfly.domain.reaction.controller;

import com.sparta.filmfly.domain.reaction.dto.BadRequestDto;
import com.sparta.filmfly.domain.reaction.dto.BadResponseDto;
import com.sparta.filmfly.domain.reaction.service.BadService;
import com.sparta.filmfly.global.auth.UserDetailsImpl;
import com.sparta.filmfly.global.common.response.DataResponseDto;
import com.sparta.filmfly.global.common.response.MessageResponseDto;
import com.sparta.filmfly.global.common.response.ResponseUtils;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/bad")
public class BadController {

    private final BadService badService;

    /**
     * 싫어요 추가
     */
    @PostMapping
    public ResponseEntity<DataResponseDto<BadResponseDto>> addBad(
        @AuthenticationPrincipal UserDetailsImpl userDetails,
        @Valid @RequestBody BadRequestDto requestDto
    ) {
        BadResponseDto responseDto = badService.addBad(userDetails.getUser(), requestDto);
        return ResponseUtils.success(responseDto);
    }

    /**
     * 싫어요 취소
     */
    @DeleteMapping
    public ResponseEntity<DataResponseDto<BadResponseDto>> removeBad(
        @AuthenticationPrincipal UserDetailsImpl userDetails,
        @Valid @RequestBody BadRequestDto requestDto
    ) {
        BadResponseDto responseDto = badService.removeBad(userDetails.getUser(), requestDto);
        return ResponseUtils.success(responseDto);
    }

    /**
     * 싫어요 누른 유저인지 확인
     */
    @PostMapping("/check")
    public ResponseEntity<DataResponseDto<Boolean>> checkBadByUser(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @Valid @RequestBody BadRequestDto requestDto
    ) {
        Boolean check = badService.checkBadByUser(userDetails.getUser(), requestDto);
        return ResponseUtils.success(check);
    }
}