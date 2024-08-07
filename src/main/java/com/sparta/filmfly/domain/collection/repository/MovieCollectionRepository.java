package com.sparta.filmfly.domain.collection.repository;

import com.sparta.filmfly.domain.collection.entity.MovieCollection;
import com.sparta.filmfly.global.common.response.ResponseCodeEnum;
import com.sparta.filmfly.global.exception.custom.detail.AlreadyExistsException;
import com.sparta.filmfly.global.exception.custom.detail.NotFoundException;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MovieCollectionRepository extends JpaRepository<MovieCollection, Long> {

    boolean existsByCollectionIdAndMovieId(Long collectionId, Long movieId);

    default void existsByCollectionIdAndMovieIdOrElseThrow(Long collectionId, Long movieId) {
        if (existsByCollectionIdAndMovieId(collectionId, movieId)) {
            throw new AlreadyExistsException(ResponseCodeEnum.MOVIE_COLLECTION_ALREADY_EXISTS);
        }
    }

    List<MovieCollection> findByCollection_id(Long collectionId);

    Optional<MovieCollection> findByCollectionIdAndMovieId(Long collectionId, Long movieId);

    default MovieCollection findByCollectionIdAndMovieIdOrElseThrow(Long collectionId, Long movieId) {
        return findByCollectionIdAndMovieId(collectionId, movieId).orElseThrow(
                () -> new NotFoundException(ResponseCodeEnum.MOVIE_COLLECTION_NOT_FOUND)
        );
    }
}