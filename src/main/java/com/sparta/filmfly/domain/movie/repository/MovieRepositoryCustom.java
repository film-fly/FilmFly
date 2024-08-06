package com.sparta.filmfly.domain.movie.repository;

import com.sparta.filmfly.domain.movie.dto.MovieReactionsResponseDto;
import com.sparta.filmfly.domain.movie.dto.MovieResponseDto;
import com.sparta.filmfly.domain.movie.dto.MovieSearchCond;
import com.sparta.filmfly.domain.movie.entity.Movie;
import com.sparta.filmfly.global.common.response.PageResponseDto;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface MovieRepositoryCustom {
    List<Movie> findByGenreIds(List<Integer> genreIds);
    PageResponseDto<List<MovieReactionsResponseDto>> getPageMovieBySearchCond(MovieSearchCond searchOptions, Pageable pageable);
}