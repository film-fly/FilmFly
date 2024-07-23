package com.sparta.filmfly.domain.officeboard.repository;

import com.sparta.filmfly.domain.officeboard.entity.OfficeBoard;
import com.sparta.filmfly.global.common.response.ResponseCodeEnum;
import com.sparta.filmfly.global.exception.custom.detail.NotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OfficeBoardRepository extends JpaRepository<OfficeBoard, Long> {

    default OfficeBoard findByIdOrElseThrow(Long boardId) {
        return findById(boardId)
                .orElseThrow(() -> new NotFoundException(ResponseCodeEnum.BOARD_NOT_FOUND));
    }

    Page<OfficeBoard> findAllByOrderByCreatedAtDesc(Pageable pageable);
}