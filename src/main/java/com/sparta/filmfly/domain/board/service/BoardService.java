package com.sparta.filmfly.domain.board.service;

import com.sparta.filmfly.domain.board.dto.BoardRequestDto;
import com.sparta.filmfly.domain.board.dto.BoardResponseDto;
import com.sparta.filmfly.domain.board.entity.Board;
import com.sparta.filmfly.domain.board.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;

    @Transactional
    public BoardResponseDto create(BoardRequestDto requestDto) { //, User user
        // 활동 정지 당한 유저라면 에러 추가
        // user.validateExam();
        Board entity = requestDto.toEntity();

        Board savedBoard = boardRepository.save(entity);

        return BoardResponseDto.fromEntity(savedBoard);
    }
}