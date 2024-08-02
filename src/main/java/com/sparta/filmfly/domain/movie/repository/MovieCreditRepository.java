package com.sparta.filmfly.domain.movie.repository;

import com.sparta.filmfly.domain.movie.entity.Credit;
import com.sparta.filmfly.domain.movie.entity.Movie;
import com.sparta.filmfly.domain.movie.entity.MovieCredit;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MovieCreditRepository extends JpaRepository<MovieCredit, Long> {
    Optional<MovieCredit> findByMovieAndCredit(Movie movie, Credit credit);

    List<MovieCredit> findByCredit(Credit credit);

}
