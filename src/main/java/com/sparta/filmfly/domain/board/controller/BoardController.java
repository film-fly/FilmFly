package com.sparta.filmfly.domain.board.controller;

import com.sparta.filmfly.domain.board.dto.BoardPageResponseDto;
import com.sparta.filmfly.domain.board.dto.BoardRequestDto;
import com.sparta.filmfly.domain.board.dto.BoardResponseDto;
import com.sparta.filmfly.domain.board.entity.Board;
import com.sparta.filmfly.domain.board.service.BoardService;
import com.sparta.filmfly.global.common.response.DataResponseDto;
import com.sparta.filmfly.global.common.response.ResponseUtils;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/board")
public class BoardController {

    private final BoardService boardService;

    //보드 생성
    @PostMapping
    public ResponseEntity<DataResponseDto<BoardResponseDto>> create(
            @Valid @RequestBody BoardRequestDto requestDto
            //@AuthenticationPrincipal UserDetailsImpl userDetails,
    ) {
        BoardResponseDto responseDto = boardService.create(requestDto);
        return ResponseUtils.success(responseDto);
    }

    //조회
    @GetMapping("/{boardId}")
    public ResponseEntity<DataResponseDto<BoardResponseDto>> read(
            @PathVariable Long boardId
    ) {
        BoardResponseDto responseDto = boardService.read(boardId);
        return ResponseUtils.success(responseDto);
    }

    //페이징 조회
    //http://localhost:8080/board/page?pageNum=1&size=5
    @GetMapping("/page")
    public ResponseEntity<DataResponseDto<BoardPageResponseDto>> reads(
            @RequestParam(value = "pageNum", required = false, defaultValue = "1") final Integer pageNum,
            @RequestParam(value = "size", required = false, defaultValue = "5") final Integer size
    ) {
        Pageable pageable = PageRequest.of(pageNum-1, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        BoardPageResponseDto responseDto = boardService.reads(pageable);
        return ResponseUtils.success(responseDto);
    }

    //보드 수정
    @PatchMapping("/{boardId}")
    public ResponseEntity<DataResponseDto<BoardResponseDto>> update(
            @PathVariable Long boardId,
            @Valid @RequestBody BoardRequestDto requestDto
            //@AuthenticationPrincipal UserDetailsImpl userDetails,
    ) {
        BoardResponseDto responseDto = boardService.update(boardId,requestDto);
        return ResponseUtils.success(responseDto);
    }

}