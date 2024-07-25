package com.sparta.filmfly.domain.favorite.service;

import com.sparta.filmfly.domain.favorite.entity.Favorite;
import com.sparta.filmfly.domain.favorite.repository.FavoriteRepository;
import com.sparta.filmfly.domain.movie.entity.Movie;
import com.sparta.filmfly.domain.movie.repository.MovieRepository;
import com.sparta.filmfly.domain.user.entity.User;
import com.sparta.filmfly.global.common.response.ResponseCodeEnum;
import com.sparta.filmfly.global.exception.custom.detail.AlreadyExistsException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class FavoriteService {

    private final FavoriteRepository favoriteRepository;
    private final MovieRepository movieRepository;

    public void createFavorite(User user, Long movieId) {
        Movie movie = movieRepository.findByIdOrElseThrow(movieId);
        if(favoriteRepository.existsFavoriteByMovieIdAndUserId(movieId, user.getId())) {
            throw new AlreadyExistsException(ResponseCodeEnum.FAVORITE_ALREADY_EXISTS);
        }
        Favorite favorite = Favorite.builder()
                .movie(movie)
                .user(user)
                .build();
        favoriteRepository.save(favorite);
    }
}