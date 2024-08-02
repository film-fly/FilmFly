package com.sparta.filmfly.domain.board.controller;

import com.sparta.filmfly.domain.board.dto.*;
import com.sparta.filmfly.domain.board.service.BoardService;
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
@RequestMapping("/boards")
public class BoardController {

    private final BoardService boardService;
    /**
     * 보드 생성
     */
    @PostMapping
    public ResponseEntity<DataResponseDto<BoardResponseDto>> createBoard(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @Valid @RequestBody BoardRequestDto requestDto
    ) {
        log.error("test : {}",requestDto.getContent());
        BoardResponseDto responseDto = boardService.createBoard(userDetails.getUser(),requestDto);
        return ResponseUtils.success(responseDto);
    }

    /**
     * 보드 조회
     */
    @GetMapping("/{boardId}")
    public ResponseEntity<DataResponseDto<BoardResponseDto>> getBoard(
            @PathVariable Long boardId
    ) {
        BoardResponseDto responseDto = boardService.getBoard(boardId);
        return ResponseUtils.success(responseDto);
    }

    /**
     * 보드 페이징 조회
     * https://localhost/boards?pageNum=1&filterGoodCount=0&filterHits=0&search=제목
     */
    @GetMapping
    public ResponseEntity<DataResponseDto<BoardPageResponseDto >> getPageBoard(
            @RequestParam(value = "pageNum", required = false, defaultValue = "1") final Integer pageNum,
            @RequestParam(value = "filterGoodCount", required = false) final Long filterGoodCount,
            @RequestParam(value = "filterHits", required = false) final Long filterHits,
            @RequestParam(value = "search", required = false) final String search
            //@RequestParam(value = "size", required = false, defaultValue = "10") final Integer size
    ) {
        Integer size = 10;
        BoardPageResponseDto  responseDto = boardService.getPageBoard(pageNum,size,filterGoodCount,filterHits,search);
        return ResponseUtils.success(responseDto);
    }

    /**
     * 보드 수정 권한 확인
     */
    @GetMapping("/{boardId}/edit-permission")
    public ResponseEntity<DataResponseDto<Boolean>> checkEditBoardPermission(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @PathVariable Long boardId
    ) {
        Boolean response = boardService.checkEditBoardPermission(userDetails.getUser(),boardId);
        return ResponseUtils.success(response);
    }

    /**
     * 보드 수정 페이지 정보
     */
    @GetMapping("/{boardId}/for-update")
    public ResponseEntity<DataResponseDto<BoardUpdateResponseDto>> forUpdateBoard(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @PathVariable Long boardId
    ) {
        BoardUpdateResponseDto responseDto = boardService.forUpdateBoard(userDetails.getUser(),boardId);
        return ResponseUtils.success(responseDto);
    }

    /**
     * 보드 수정
     */
    @PatchMapping("/{boardId}")
    public ResponseEntity<DataResponseDto<BoardResponseDto>> updateBoard(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @Valid @RequestBody BoardRequestDto requestDto,
            @PathVariable Long boardId
    ) {
        BoardResponseDto responseDto = boardService.updateBoard(userDetails.getUser(),requestDto,boardId);
        return ResponseUtils.success(responseDto);
    }

    /**
     * 보드 삭제
     */
    @DeleteMapping("/{boardId}")
    public ResponseEntity<DataResponseDto<String>> deleteBoard(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @PathVariable Long boardId
    ) {
        String responseDto = boardService.deleteBoard(userDetails.getUser(),boardId);
        return ResponseUtils.success(responseDto);
    }

    /**
     * 유저의 보드 목록
     */
    @GetMapping("/users/{userId}")
    public ResponseEntity<DataResponseDto<BoardPageResponseDto>> getUsersBoard(
            @RequestParam(value = "pageNum", required = false, defaultValue = "1") final Integer pageNum,
            //@RequestParam(value = "size", required = false, defaultValue = "10") final Integer size
            @PathVariable Long userId) {
        Integer size = 10;
        BoardPageResponseDto  responseDto = boardService.getUsersBoard(pageNum,size,userId);
        return ResponseUtils.success(responseDto);
    }

}