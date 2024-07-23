package com.sparta.filmfly.domain.board.controller;

import com.sparta.filmfly.domain.board.dto.BoardPageResponseDto;
import com.sparta.filmfly.domain.board.dto.BoardRequestDto;
import com.sparta.filmfly.domain.board.dto.BoardResponseDto;
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
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping
public class BoardController {

    private final BoardService boardService;
    
    //보드 생성 + 파일 업로드
    @PostMapping(value = "/board", consumes = "multipart/form-data")
    public ResponseEntity<DataResponseDto<BoardResponseDto>> createBoard2(
            @Valid @RequestPart(value = "boardRequestDto") BoardRequestDto requestDto,
            @RequestPart(value = "files", required=false) List<MultipartFile> files,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        BoardResponseDto responseDto = boardService.createBoard(requestDto,files,userDetails.getUser());
        return ResponseUtils.success(responseDto);
    }

    //조회
    @GetMapping("/board/{boardId}")
    public ResponseEntity<DataResponseDto<BoardResponseDto>> readBoard(
            @PathVariable Long boardId
    ) {
        BoardResponseDto responseDto = boardService.readBoard(boardId);
        return ResponseUtils.success(responseDto);
    }

    //페이징 조회
    //http://localhost:8080/boards?pageNum=1&size=5
    @GetMapping("/boards")
    public ResponseEntity<DataResponseDto<BoardPageResponseDto>> readBoards(
            @RequestParam(value = "pageNum", required = false, defaultValue = "1") final Integer pageNum,
            @RequestParam(value = "size", required = false, defaultValue = "5") final Integer size
    ) {
        BoardPageResponseDto responseDto = boardService.readBoards(pageNum,size);
        return ResponseUtils.success(responseDto);
    }

    //보드 수정 + 파일
    @PatchMapping("/board/{boardId}")
    public ResponseEntity<DataResponseDto<BoardResponseDto>> updateBoard2(
            @PathVariable Long boardId,
            @Valid @RequestPart(value = "boardRequestDto") BoardRequestDto requestDto,
            @RequestPart(value = "files", required=false) List<MultipartFile> files,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        BoardResponseDto responseDto = boardService.updateBoard(boardId,files,requestDto,userDetails.getUser());
        return ResponseUtils.success(responseDto);
    }

    //보드 삭제
    @DeleteMapping("/board/{boardId}")
    public ResponseEntity<DataResponseDto<String>> deleteBoard(
            @PathVariable Long boardId,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        String responseDto = boardService.deleteBoard(boardId,userDetails.getUser());
        return ResponseUtils.success(responseDto);
    }

}