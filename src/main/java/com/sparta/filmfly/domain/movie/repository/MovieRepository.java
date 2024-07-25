package com.sparta.filmfly.domain.movie.repository;

import com.sparta.filmfly.domain.movie.entity.Movie;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MovieRepository extends JpaRepository<Movie, Long> {

    List<Movie> findMoviesByTitle(String keyword);

    default Movie findByIdOrElseThrow(Long id) {
        return findById(id).orElseThrow();
    }
}