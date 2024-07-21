package com.sparta.filmfly.domain.board.service;

import com.sparta.filmfly.domain.board.dto.BoardPageResponseDto;
import com.sparta.filmfly.domain.board.dto.BoardRequestDto;
import com.sparta.filmfly.domain.board.dto.BoardResponseDto;
import com.sparta.filmfly.domain.board.entity.Board;
import com.sparta.filmfly.domain.board.repository.BoardRepository;
import com.sparta.filmfly.domain.user.entity.User;
import com.sparta.filmfly.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    @Transactional(readOnly = true)
    public BoardResponseDto read(Long boardId) {
        Board board = boardRepository.findByIdOrElseThrow(boardId);

        return BoardResponseDto.fromEntity(board);
    }

    @Transactional(readOnly = true)
    public BoardPageResponseDto reads(Pageable pageable) {
        Page<Board> boards = boardRepository.findAll(pageable);
        //QueryDSL 최적화 수정

        //List 형식 totalPages,size,content,number 등 필요한 정보만 보내는 PageResponse
        return BoardPageResponseDto.fromPage(boards);
    }

    @Transactional
    public BoardResponseDto update(Long boardId, BoardRequestDto requestDto) {
        // 활동 정지 당한 유저라면 에러 추가
        // user.validateExam();

        Board board = boardRepository.findByIdOrElseThrow(boardId); //보드 존재 여부 확인
        //수정 요청한 유저가 해당 보드의 소유주인지 확인하기
        //if(board.getUser().getId() == user.getId()) {}
        board.update(requestDto);
        Board updatedBoard = boardRepository.save(board);

        return BoardResponseDto.fromEntity(updatedBoard);
    }

    public String delete(Long boardId) {
        // 활동 정지 당한 유저라면 에러 추가
        // user.validateExam();

        Board board = boardRepository.findByIdOrElseThrow(boardId); //보드 존재 여부 확인
        //수정 요청한 유저가 해당 보드의 소유주인지 확인하기
        //if(board.getUser().getId() == user.getId()) {}
        boardRepository.delete(board);

        return "게시물이 삭제되었습니다.";
    }
}