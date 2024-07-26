package com.sparta.filmfly.domain.reaction.controller;

import com.sparta.filmfly.domain.reaction.ReactionContentTypeEnum;
import com.sparta.filmfly.domain.reaction.dto.GoodRequestDto;
import com.sparta.filmfly.domain.reaction.service.GoodService;
import com.sparta.filmfly.global.auth.UserDetailsImpl;
import com.sparta.filmfly.global.common.response.MessageResponseDto;
import com.sparta.filmfly.global.common.response.ResponseUtils;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    public ResponseEntity<MessageResponseDto> addGood(
        @AuthenticationPrincipal UserDetailsImpl userDetails,
        @Valid @RequestBody GoodRequestDto requestDto
    ) {
        goodService.addGood(userDetails.getUser(), requestDto);
        return ResponseUtils.success();
    }

    /**
     * 좋아요 취소
     */
    @DeleteMapping
    public ResponseEntity<MessageResponseDto> removeGood(
        @AuthenticationPrincipal UserDetailsImpl userDetails,
        @Valid @RequestBody GoodRequestDto requestDto
    ) {
        goodService.removeGood(userDetails.getUser(), requestDto);
        return ResponseUtils.success();
    }
}