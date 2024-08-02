package com.sparta.filmfly.domain.movie.repository;

import com.sparta.filmfly.domain.movie.entity.Credit;

import java.util.List;

public interface CreditRepositoryCustom {
    List<Credit> findByMovieId(Long movieId);
}
