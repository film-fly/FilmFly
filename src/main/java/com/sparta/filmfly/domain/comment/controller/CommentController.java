package com.sparta.filmfly.domain.comment.controller;

import com.sparta.filmfly.domain.comment.dto.CommentPageResponseDto;
import com.sparta.filmfly.domain.comment.dto.CommentRequestDto;
import com.sparta.filmfly.domain.comment.dto.CommentResponseDto;
import com.sparta.filmfly.domain.comment.service.CommentService;
import com.sparta.filmfly.global.auth.UserDetailsImpl;
import com.sparta.filmfly.global.common.response.DataResponseDto;
import com.sparta.filmfly.global.common.response.ResponseUtils;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/boards/{boardId}")
public class CommentController {
    private final CommentService commentService;

    /**
     * 댓글 생성
     */
    @PostMapping("/comments")
    public ResponseEntity<DataResponseDto<CommentResponseDto>> createComment(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @Valid @RequestBody CommentRequestDto requestDto,
            @PathVariable Long boardId
    ) {
        CommentResponseDto responseDto = commentService.createComment(userDetails.getUser(),requestDto,boardId);
        return ResponseUtils.success(responseDto);
    }

    /**
     * 댓글 조회
     */
    @GetMapping("/comments/{commentId}")
    public ResponseEntity<DataResponseDto<CommentResponseDto>> getComment(
            @PathVariable Long boardId,
            @PathVariable Long commentId
    ) {
        CommentResponseDto responseDto = commentService.getComment(boardId, commentId);
        return ResponseUtils.success(responseDto);
    }

    /**
     * 댓글 페이지 조회
     * http://localhost:8080/boards/{boardId}/comments?pageNum=1&size=50
     */
    @GetMapping("/comments")
    public ResponseEntity<DataResponseDto<CommentPageResponseDto>> gerPageComment(
            @PathVariable Long boardId,
            @RequestParam(value = "pageNum", required = false, defaultValue = "1") final Integer pageNum,
            @RequestParam(value = "size", required = false, defaultValue = "50") final Integer size
    ) {
        CommentPageResponseDto responseDto = commentService.gerPageComment(pageNum,size,boardId);
        return ResponseUtils.success(responseDto);
    }

    /**
     * 댓글 수정
     */
    @PatchMapping("/comments/{commentId}")
    public ResponseEntity<DataResponseDto<CommentResponseDto>> updateComment(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @Valid @RequestBody CommentRequestDto requestDto,
            @PathVariable Long boardId,
            @PathVariable Long commentId
    ) {
        CommentResponseDto responseDto = commentService.updateComment(userDetails.getUser(),requestDto,boardId,commentId);
        return ResponseUtils.success(responseDto);
    }

    /**
     * 댓글 삭제
     */
    @DeleteMapping("/comments/{commentId}")
    public ResponseEntity<DataResponseDto<String>> deleteComment(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @PathVariable Long boardId,
            @PathVariable Long commentId
    ) {
        String responseDto = commentService.deleteComment(userDetails.getUser(),boardId,commentId);
        return ResponseUtils.success(responseDto);
    }

}