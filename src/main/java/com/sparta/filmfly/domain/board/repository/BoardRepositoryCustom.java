package com.sparta.filmfly.domain.board.repository;

import com.sparta.filmfly.domain.board.dto.BoardPageResponseDto;
import org.springframework.data.domain.Pageable;

public interface BoardRepositoryCustom {
    BoardPageResponseDto findAllWithFilters(Pageable pageable, Long filterGoodCount, Long filterHits, String search);
    BoardPageResponseDto findAllByUserId(Long userId,Pageable pageable);
}
