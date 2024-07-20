package com.sparta.filmfly.domain.board.controller;

import com.sparta.filmfly.domain.board.dto.BoardRequestDto;
import com.sparta.filmfly.domain.board.dto.BoardResponseDto;
import com.sparta.filmfly.domain.board.service.BoardService;
import com.sparta.filmfly.global.common.response.DataResponseDto;
import com.sparta.filmfly.global.common.response.ResponseUtils;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/board")
public class BoardController {

    private final BoardService boardService;

    /**
     * 보드 생성
     */
    @PostMapping
    public ResponseEntity<DataResponseDto<BoardResponseDto>> create(
            //@AuthenticationPrincipal UserDetailsImpl userDetails,
            @Valid @RequestBody BoardRequestDto requestDto
    ) {
        BoardResponseDto responseDto = boardService.create(requestDto);
        return ResponseUtils.success(responseDto);
    }

}