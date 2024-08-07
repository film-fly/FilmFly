package com.sparta.filmfly.domain.movie.repository;

import com.sparta.filmfly.domain.movie.entity.Movie;
import com.sparta.filmfly.global.common.response.ResponseCodeEnum;
import com.sparta.filmfly.global.exception.custom.detail.NotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MovieRepository extends JpaRepository<Movie, Long>, MovieRepositoryCustom {

    Page<Movie> findAllByOrderByPopularityDesc(Pageable pageable);

    default Movie findByIdOrElseThrow(Long id) {
        return findById(id).orElseThrow(
                () -> new NotFoundException(ResponseCodeEnum.MOVIE_NOT_FOUND)
        );
    }

    List<Movie> findMoviesByTitleContaining(String keyword, Pageable pageable);

    long countByTitleContaining(String keyword);

    List<Movie> findByIdIn(List<Long> ids);
}