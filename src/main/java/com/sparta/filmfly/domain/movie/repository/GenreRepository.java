package com.sparta.filmfly.domain.movie.repository;

import com.sparta.filmfly.domain.movie.entity.Genre;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GenreRepository extends JpaRepository<Genre, Long> {

    List<Genre> findAllByNameContaining(String name);
}
