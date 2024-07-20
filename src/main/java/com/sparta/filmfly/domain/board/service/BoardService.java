package com.sparta.filmfly.domain.board.service;

import com.sparta.filmfly.domain.board.dto.BoardRequestDto;
import com.sparta.filmfly.domain.board.dto.BoardResponseDto;
import com.sparta.filmfly.domain.board.entity.Board;
import com.sparta.filmfly.domain.board.repository.BoardRepository;
import com.sparta.filmfly.domain.user.entity.User;
import com.sparta.filmfly.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;
    private final UserRepository userRepository; // 로그인 기능 완료되면 없애기

    @Transactional
    public BoardResponseDto create(BoardRequestDto requestDto) { //, User user
        // 활동 정지 당한 유저라면 에러 추가
        // user.validateExam();
        User findUser = userRepository.findByIdOrElseThrow(1L); // 로그인 기능 완료되면 없애기
        Board entity = requestDto.toEntity(findUser);

        Board savedBoard = boardRepository.save(entity);

        return BoardResponseDto.fromEntity(savedBoard);
    }
}