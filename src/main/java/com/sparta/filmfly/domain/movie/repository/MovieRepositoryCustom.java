package com.sparta.filmfly.domain.movie.repository;

import com.sparta.filmfly.domain.movie.dto.MovieReactionsResponseDto;
import com.sparta.filmfly.domain.movie.dto.MovieSearchCond;
import com.sparta.filmfly.domain.movie.entity.Movie;
import com.sparta.filmfly.global.common.response.PageResponseDto;
import java.util.List;
import org.springframework.data.domain.Pageable;

public interface MovieRepositoryCustom {
    List<Movie> findByGenreIds(List<Integer> genreIds);
    PageResponseDto<List<MovieReactionsResponseDto>> getPageMovieBySearchCond(MovieSearchCond searchOptions, Pageable pageable);
}