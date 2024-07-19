package com.sparta.filmfly.domain.movie.service;

import com.sparta.filmfly.domain.movie.repository.FavoriteRepository;
import com.sparta.filmfly.domain.movie.repository.MovieRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class MovieService {

    private final MovieRepository movieRepository;
}