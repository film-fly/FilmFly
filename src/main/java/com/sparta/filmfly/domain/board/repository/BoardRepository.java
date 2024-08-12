package com.sparta.filmfly.domain.board.repository;

import com.sparta.filmfly.domain.board.entity.Board;
import com.sparta.filmfly.global.common.response.ResponseCodeEnum;
import com.sparta.filmfly.global.exception.custom.detail.NotFoundException;
import java.util.List;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardRepository extends JpaRepository<Board, Long>, BoardRepositoryCustom  {
    default Board findByIdOrElseThrow(Long boardId) {
        return findById(boardId)
                .orElseThrow(() -> new NotFoundException(ResponseCodeEnum.BOARD_NOT_FOUND));
    }
    default void existsByIdOrElseThrow(Long commentId) {
        if (!existsById(commentId)) {
            throw new NotFoundException(ResponseCodeEnum.BOARD_NOT_FOUND);
        }
    }

    boolean existsByIdAndUserId(Long id, Long userId);

    long count();

    List<Board> findByIdGreaterThan(Long lastProcessedId, PageRequest of);
}