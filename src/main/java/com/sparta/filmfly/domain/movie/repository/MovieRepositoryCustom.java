package com.sparta.filmfly.domain.movie.repository;

import com.sparta.filmfly.domain.movie.entity.Movie;
import org.springframework.data.relational.core.query.Criteria;

import java.util.List;

public interface MovieRepositoryCustom {
    List<Movie> findByGenreIds(List<Integer> genreIds);
}
