package com.sparta.filmfly.domain.favorite.repository;

import com.sparta.filmfly.domain.favorite.entity.Favorite;
import com.sparta.filmfly.global.common.response.ResponseCodeEnum;
import com.sparta.filmfly.global.exception.custom.detail.AlreadyExistsException;
import com.sparta.filmfly.global.exception.custom.detail.NotFoundException;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FavoriteRepository extends JpaRepository<Favorite, Long> {

    List<Favorite> findAllByUserId(Long userId);

    boolean existsByMovieIdAndUserId(Long movieId, Long userId);
    default void existsByMovieIdAndUserIdOrElseThrow(Long movieId, Long userId) {
        if (existsByMovieIdAndUserId(movieId, userId)) {
            throw new AlreadyExistsException(ResponseCodeEnum.FAVORITE_ALREADY_EXISTS);
        }
    }

    Optional<Favorite> findByMovieIdAndUserId(Long movieId, Long userId);
    default Favorite findByMovieIdAndUserIdOrElseThrow(Long movieId, Long userId) {
        return this.findByMovieIdAndUserId(movieId, userId)
            .orElseThrow(() -> new NotFoundException(ResponseCodeEnum.FAVORITE_NOT_FOUND));
    }

//    @Query("SELECT * FROM favorite f WHERE f.deleted_at IS NOT NULL", nativeQuery = true)
//    List<Favorite> findAllByDeletedAtIsNotNull();
//
//    boolean deleteById(long id);
}