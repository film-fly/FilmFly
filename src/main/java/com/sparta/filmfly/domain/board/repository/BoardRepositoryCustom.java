package com.sparta.filmfly.domain.board.repository;

import com.sparta.filmfly.domain.board.dto.BoardPageDto;
import com.sparta.filmfly.global.common.response.PageResponseDto;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface BoardRepositoryCustom {
    PageResponseDto<List<BoardPageDto>> findAllWithFilters(Pageable pageable, Long filterGoodCount, Long filterHits, String search);
    PageResponseDto<List<BoardPageDto>> findAllByUserId(Long userId,Pageable pageable);
}
