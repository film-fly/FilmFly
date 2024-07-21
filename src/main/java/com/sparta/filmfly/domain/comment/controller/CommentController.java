package com.sparta.filmfly.domain.comment.controller;

import com.sparta.filmfly.domain.board.dto.BoardResponseDto;
import com.sparta.filmfly.domain.comment.dto.CommentPageResponseDto;
import com.sparta.filmfly.domain.comment.dto.CommentRequestDto;
import com.sparta.filmfly.domain.comment.dto.CommentResponseDto;
import com.sparta.filmfly.domain.comment.service.CommentService;
import com.sparta.filmfly.global.common.response.DataResponseDto;
import com.sparta.filmfly.global.common.response.ResponseUtils;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/board/{boardId}")
public class CommentController {
    private final CommentService commentService;

    //댓글 생성
    @PostMapping("/comment")
    public ResponseEntity<DataResponseDto<CommentResponseDto>> createComment(
            @PathVariable Long boardId,
            @Valid @RequestBody CommentRequestDto requestDto
            //@AuthenticationPrincipal UserDetailsImpl userDetails,
    ) {
        CommentResponseDto responseDto = commentService.createComment(boardId,requestDto);
        return ResponseUtils.success(responseDto);
    }

    //조회
    @GetMapping("/comment/{commentId}")
    public ResponseEntity<DataResponseDto<CommentResponseDto>> readComment(
            @PathVariable Long boardId,
            @PathVariable Long commentId
    ) {
        CommentResponseDto responseDto = commentService.readComment(boardId, commentId);
        return ResponseUtils.success(responseDto);
    }

    //페이징 조회
    //http://localhost:8080/board/{boardId}/comment?pageNum=1&size=50
    @GetMapping("/comments")
    public ResponseEntity<DataResponseDto<CommentPageResponseDto>> readsComment(
            @PathVariable Long boardId,
            @RequestParam(value = "pageNum", required = false, defaultValue = "1") final Integer pageNum,
            @RequestParam(value = "size", required = false, defaultValue = "50") final Integer size
    ) {
        Pageable pageable = PageRequest.of(pageNum-1, size, Sort.by(Sort.Direction.ASC, "createdAt"));
        //정렬은 생성 시간, 받은 댓글은 수정 시간
        CommentPageResponseDto responseDto = commentService.readsComment(boardId, pageable);
        return ResponseUtils.success(responseDto);
    }

}