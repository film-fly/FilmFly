package com.sparta.filmfly.domain.reaction.controller;

import com.sparta.filmfly.domain.reaction.dto.BadRequestDto;
import com.sparta.filmfly.domain.reaction.dto.GoodRequestDto;
import com.sparta.filmfly.domain.reaction.dto.GoodResponseDto;
import com.sparta.filmfly.domain.reaction.service.GoodService;
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
@RequestMapping("/good")
public class GoodController {

    private final GoodService goodService;

    /**
     * 좋아요 추가
     */
    @PostMapping
    public ResponseEntity<DataResponseDto<GoodResponseDto>> addGood(
        @AuthenticationPrincipal UserDetailsImpl userDetails,
        @Valid @RequestBody GoodRequestDto requestDto
    ) {
        GoodResponseDto responseDto = goodService.addGood(userDetails.getUser(), requestDto);
        return ResponseUtils.success(responseDto);
    }

    /**
     * 좋아요 취소
     */
    @DeleteMapping
    public ResponseEntity<DataResponseDto<GoodResponseDto>> removeGood(
        @AuthenticationPrincipal UserDetailsImpl userDetails,
        @Valid @RequestBody GoodRequestDto requestDto
    ) {
        GoodResponseDto responseDto = goodService.removeGood(userDetails.getUser(), requestDto);
        return ResponseUtils.success(responseDto);
    }

    /**
     * 좋아요 누른 유저인지 확인
     */
    @PostMapping("/check")
    public ResponseEntity<DataResponseDto<Boolean>> checkGoodByUser(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @Valid @RequestBody BadRequestDto requestDto
    ) {
        Boolean check = goodService.checkGoodByUser(userDetails.getUser(), requestDto);
        return ResponseUtils.success(check);
    }
}