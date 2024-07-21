package com.sparta.filmfly.domain.comment.controller;

import com.sparta.filmfly.domain.comment.dto.CommentRequestDto;
import com.sparta.filmfly.domain.comment.dto.CommentResponseDto;
import com.sparta.filmfly.domain.comment.service.CommentService;
import com.sparta.filmfly.global.common.response.DataResponseDto;
import com.sparta.filmfly.global.common.response.ResponseUtils;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/board/{boardId}/comment")
public class CommentController {
    private final CommentService commentService;

    //댓글 생성
    @PostMapping
    public ResponseEntity<DataResponseDto<CommentResponseDto>> createComment(
            @PathVariable Long boardId,
            @Valid @RequestBody CommentRequestDto requestDto
            //@AuthenticationPrincipal UserDetailsImpl userDetails,
    ) {
        CommentResponseDto responseDto = commentService.createComment(boardId,requestDto);
        return ResponseUtils.success(responseDto);
    }

}