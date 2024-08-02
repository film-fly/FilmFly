package com.sparta.filmfly.domain.favorite.service;

import com.sparta.filmfly.domain.favorite.entity.Favorite;
import com.sparta.filmfly.domain.favorite.repository.FavoriteRepository;
import com.sparta.filmfly.domain.movie.dto.MovieResponseDto;
import com.sparta.filmfly.domain.movie.entity.Movie;
import com.sparta.filmfly.domain.movie.repository.MovieRepository;
import com.sparta.filmfly.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class FavoriteService {

    private final FavoriteRepository favoriteRepository;
    private final MovieRepository movieRepository;

    /**
    * 영화 찜 등록하기
    */
    public void createFavorite(User user, Long movieId) {
        // DB 존재 검증
        Movie movie = movieRepository.findByIdOrElseThrow(movieId);

        favoriteRepository.existsByMovieIdAndUserIdOrElseThrow(movieId, user.getId());

        Favorite favorite = Favorite.builder()
                .movie(movie)
                .user(user)
                .build();
        favoriteRepository.save(favorite);
    }

    /**
    * 자신이 찜한 영화 조회
    */
    public List<MovieResponseDto> getFavorite(User user) {
        List<Favorite> favoriteList = favoriteRepository.findAllByUserId(user.getId());
        List<Movie> movieList = favoriteList.stream().map(
                favorite -> movieRepository.findByIdOrElseThrow(favorite.getMovie().getId())
        ).toList();
        return movieList.stream().map(MovieResponseDto::fromEntity).toList();
    }

    /**
    * 찜한 영화 취소하기
    */
    public void deleteFavorite(User user, Long movieId) {
        Favorite favorite = favoriteRepository.findByMovieIdAndUserIdOrElseThrow(movieId, user.getId());
        favoriteRepository.delete(favorite);
    }
}