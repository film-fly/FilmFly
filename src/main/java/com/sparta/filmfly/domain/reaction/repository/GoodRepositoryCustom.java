package com.sparta.filmfly.domain.reaction.repository;

import com.sparta.filmfly.domain.reaction.dto.ReactionMovieResponseDto;
import com.sparta.filmfly.global.common.response.PageResponseDto;
import java.util.List;
import org.springframework.data.domain.Pageable;

public interface GoodRepositoryCustom {
    PageResponseDto<List<ReactionMovieResponseDto>> getPageMovieByUserGood(Long userId, Pageable pageable);
}