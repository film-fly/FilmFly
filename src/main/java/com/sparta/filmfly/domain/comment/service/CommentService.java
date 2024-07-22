package com.sparta.filmfly.domain.comment.service;

import com.sparta.filmfly.domain.board.entity.Board;
import com.sparta.filmfly.domain.board.repository.BoardRepository;
import com.sparta.filmfly.domain.comment.dto.CommentPageResponseDto;
import com.sparta.filmfly.domain.comment.dto.CommentRequestDto;
import com.sparta.filmfly.domain.comment.dto.CommentResponseDto;
import com.sparta.filmfly.domain.comment.entity.Comment;
import com.sparta.filmfly.domain.comment.repository.CommentRepository;
import com.sparta.filmfly.domain.user.entity.User;
import com.sparta.filmfly.domain.user.entity.UserRoleEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class CommentService {
    private final BoardRepository boardRepository;
    private final CommentRepository commentRepository;

    @Transactional
    public CommentResponseDto createComment(Long boardId, CommentRequestDto requestDto, User user) {
        user.validateUserStatus(); //탈퇴,정지 상태 비교

        Board board = boardRepository.findByIdOrElseThrow(boardId);
        Comment entity = requestDto.toEntity(user,board);
        Comment savedComment = commentRepository.save(entity);

        return CommentResponseDto.fromEntity(savedComment);
    }

    @Transactional(readOnly = true)
    public CommentResponseDto readComment(Long boardId, Long commentId) {
        Board board = boardRepository.findByIdOrElseThrow(boardId); //게시판 경로 확인
        Comment comment = commentRepository.findByIdOrElseThrow(commentId);

        return CommentResponseDto.fromEntity(comment);
    }

    @Transactional(readOnly = true)
    public CommentPageResponseDto readsComment(Long boardId, Pageable pageable) {
        Board board = boardRepository.findByIdOrElseThrow(boardId);
        Page<Comment> comments = commentRepository.findAll(pageable);
        //QueryDSL 최적화 수정

        //List 형식 totalPages,size,content,number 등 필요한 정보만 보내는 PageResponse
        return CommentPageResponseDto.fromPage(comments);
    }

    @Transactional
    public CommentResponseDto updateComment(Long boardId, Long commentId, CommentRequestDto requestDto, User user) {
        user.validateUserStatus();
        Board board = boardRepository.findByIdOrElseThrow(boardId); //게시판 경로 확인
        Comment comment = commentRepository.findByIdOrElseThrow(commentId); //보드 존재 여부 확인
        comment.validateOwner(user); //요청한 유저가 해당 댓글 소유주인지 확인

        comment.update(requestDto);
        Comment updatedComment = commentRepository.save(comment);

        return CommentResponseDto.fromEntity(updatedComment);
    }

    @Transactional
    public String deleteComment(Long boardId, Long commentId, User user) {
        user.validateUserStatus();
        Board board = boardRepository.findByIdOrElseThrow(boardId);
        Comment comment = commentRepository.findByIdOrElseThrow(commentId);
        if(user.getUserRole() == UserRoleEnum.ROLE_USER) //관리자면 삭제 가능하게
            comment.validateOwner(user);

        commentRepository.delete(comment);

        return "댓글이 삭제되었습니다.";
    }

}