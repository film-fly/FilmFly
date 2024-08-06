package com.sparta.filmfly.domain.comment.service;

import com.sparta.filmfly.domain.board.entity.Board;
import com.sparta.filmfly.domain.board.repository.BoardRepository;
import com.sparta.filmfly.domain.comment.dto.CommentRequestDto;
import com.sparta.filmfly.domain.comment.dto.CommentResponseDto;
import com.sparta.filmfly.domain.comment.dto.CommentUpdateResponseDto;
import com.sparta.filmfly.domain.comment.entity.Comment;
import com.sparta.filmfly.domain.comment.repository.CommentRepository;
import com.sparta.filmfly.domain.reaction.ReactionContentTypeEnum;
import com.sparta.filmfly.domain.reaction.service.BadService;
import com.sparta.filmfly.domain.reaction.service.GoodService;
import com.sparta.filmfly.domain.user.entity.User;
import com.sparta.filmfly.domain.user.entity.UserRoleEnum;
import com.sparta.filmfly.global.common.response.PageResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CommentService {
    private final BoardRepository boardRepository;
    private final CommentRepository commentRepository;
    private final GoodService goodService;
    private final BadService badService;

    /**
     * 댓글 생성
     */
    @Transactional
    public CommentResponseDto createComment(User user, CommentRequestDto requestDto, Long boardId) {
        Board board = boardRepository.findByIdOrElseThrow(boardId);

        Comment entity = requestDto.toEntity(user,board);
        Comment savedComment = commentRepository.save(entity);
        return CommentResponseDto.fromEntity(savedComment,0L,0L);
    }

    /**
     * 댓글 조회
     */
    @Transactional(readOnly = true)
    public CommentResponseDto getComment(Long commentId) {
        Comment comment = commentRepository.findByIdOrElseThrow(commentId);

        Long goodCount = goodService.getCountByTypeTypeId(ReactionContentTypeEnum.COMMENT,commentId);
        Long badCount = badService.getCountByTypeTypeId(ReactionContentTypeEnum.COMMENT,commentId);
        return CommentResponseDto.fromEntity(comment,goodCount,badCount);
    }

    /**
     * 댓글 페이지 조회
     */
    @Transactional(readOnly = true)
    public PageResponseDto<List<CommentResponseDto>> gerPageComment(Long boardId, Pageable pageable) {
        //정렬은 생성 시간, get으로 넘겨주는 댓글은 수정 시간
        boardRepository.existsByIdOrElseThrow(boardId); // 보드가 존재하지 않으면 에러

        return commentRepository.findAllByBoardIdWithReactions(boardId, pageable);
    }

    /**
     * 유저의 댓글 조회
     */
    public PageResponseDto<List<CommentResponseDto>> getUsersComments(Long userId, Pageable pageable) {
        return commentRepository.findAllByUserId(userId,pageable);
    }

    /**
     * 댓글 수정 권한 확인
     */
    public CommentUpdateResponseDto forUpdateComment(User user, Long commentId) {
        Comment comment = commentRepository.findByIdOrElseThrow(commentId);
        comment.validateOwner(user);
        return CommentUpdateResponseDto.fromEntity(comment);
    }

    /**
     * 댓글 수정
     */
    @Transactional
    public CommentResponseDto updateComment(User user, CommentRequestDto requestDto, Long commentId) {
        Comment comment = commentRepository.findByIdOrElseThrow(commentId);
        comment.validateOwner(user);

        comment.update(requestDto);
        Comment updatedComment = commentRepository.save(comment);
        Long goodCount = goodService.getCountByTypeTypeId(ReactionContentTypeEnum.COMMENT,commentId);
    Long badCount = badService.getCountByTypeTypeId(ReactionContentTypeEnum.COMMENT,commentId);

        return CommentResponseDto.fromEntity(updatedComment,goodCount,badCount);
    }

    /**
     * 댓글 삭제
     */
    @Transactional
    public String deleteComment(User user, Long commentId) {
        Comment comment = commentRepository.findByIdOrElseThrow(commentId);

        if(user.getUserRole() == UserRoleEnum.ROLE_USER) { //관리자면 삭제 가능하게
            comment.validateOwner(user);
        }

        commentRepository.delete(comment);

        return "댓글이 삭제되었습니다.";
    }
}