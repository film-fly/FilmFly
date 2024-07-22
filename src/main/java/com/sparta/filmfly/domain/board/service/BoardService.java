package com.sparta.filmfly.domain.board.service;

import com.sparta.filmfly.domain.board.dto.BoardPageResponseDto;
import com.sparta.filmfly.domain.board.dto.BoardRequestDto;
import com.sparta.filmfly.domain.board.dto.BoardResponseDto;
import com.sparta.filmfly.domain.board.entity.Board;
import com.sparta.filmfly.domain.board.repository.BoardRepository;
import com.sparta.filmfly.domain.user.entity.User;
import com.sparta.filmfly.domain.user.entity.UserRoleEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;

    @Transactional
    public BoardResponseDto createBoard(BoardRequestDto requestDto, User user) {
        user.validateUserStatus(); //탈퇴,정지 상태 비교

        Board entity = requestDto.toEntity(user);
        Board savedBoard = boardRepository.save(entity);

        return BoardResponseDto.fromEntity(savedBoard);
    }

    @Transactional(readOnly = true)
    public BoardResponseDto readBoard(Long boardId) {
        Board board = boardRepository.findByIdOrElseThrow(boardId);

        return BoardResponseDto.fromEntity(board);
    }

    @Transactional(readOnly = true)
    public BoardPageResponseDto readBoards(Integer pageNum, Integer size) {
        Pageable pageable = PageRequest.of(pageNum-1, size, Sort.by(Sort.Direction.DESC, "createdAt"));

        Page<Board> boards = boardRepository.findAll(pageable);
        //QueryDSL 최적화 수정

        //List 형식 totalPages,size,content,number 등 필요한 정보만 보내는 PageResponse
        return BoardPageResponseDto.fromPage(boards);
    }

    @Transactional
    public BoardResponseDto updateBoard(Long boardId, BoardRequestDto requestDto, User user) {
        user.validateUserStatus();

        Board board = boardRepository.findByIdOrElseThrow(boardId); //보드 존재 여부 확인
        board.validateOwner(user); //수정 요청한 유저가 해당 보드의 소유주인지 확인

        board.update(requestDto);
        Board updatedBoard = boardRepository.save(board);

        return BoardResponseDto.fromEntity(updatedBoard);
    }

    @Transactional
    public String deleteBoard(Long boardId, User user) {
        user.validateUserStatus();

        Board board = boardRepository.findByIdOrElseThrow(boardId);
        if(user.getUserRole() == UserRoleEnum.ROLE_USER) //관리자면 삭제 가능하게
            board.validateOwner(user);

        boardRepository.delete(board);

        return "게시물이 삭제되었습니다.";
    }
}