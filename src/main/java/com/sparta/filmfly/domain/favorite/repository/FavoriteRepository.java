package com.sparta.filmfly.domain.favorite.repository;

import com.sparta.filmfly.domain.favorite.entity.Favorite;
import com.sparta.filmfly.global.common.response.ResponseCodeEnum;
import com.sparta.filmfly.global.exception.custom.detail.AlreadyExistsException;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FavoriteRepository extends JpaRepository<Favorite, Long> {

    List<Favorite> findAllByUser_Id(Long userId);

    boolean existsFavoriteByMovieIdAndUserId(Long movieId, Long userId);

    Favorite findByUser_IdAndMovie_Id(Long userId, Long MovieId);

    default void existsFavoriteByMovieIdAndUserIdOrElseThrow(Long movieId, Long userId) {
        if (!existsFavoriteByMovieIdAndUserId(movieId, userId)) {
            throw new AlreadyExistsException(ResponseCodeEnum.FAVORITE_ALREADY_EXISTS);
        }
    }

    default Favorite findByUser_IdAndMovie_IdOrElseThrow(Long movieId, Long userId) {
        return findByUser_IdAndMovie_Id(userId, movieId);
    }

//    @Query("SELECT * FROM favorite f WHERE f.deleted_at IS NOT NULL", nativeQuery = true)
//    List<Favorite> findAllByDeletedAtIsNotNull();
//
//    boolean deleteById(long id);
}