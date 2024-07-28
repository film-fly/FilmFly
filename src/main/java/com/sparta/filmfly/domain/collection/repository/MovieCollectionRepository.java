package com.sparta.filmfly.domain.collection.repository;

import com.sparta.filmfly.domain.collection.entity.MovieCollection;
import com.sparta.filmfly.global.common.response.ResponseCodeEnum;
import com.sparta.filmfly.global.exception.custom.detail.AlreadyExistsException;
import com.sparta.filmfly.global.exception.custom.detail.NotFoundException;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MovieCollectionRepository extends JpaRepository<MovieCollection, Long> {

    boolean existsByCollection_idAndMovie_id(Long collectionId, Long movieId);

    default void existsByCollection_idAndMovie_idOrElseThrow(Long collectionId, Long movieId) {
        if (!existsByCollection_idAndMovie_id(collectionId, movieId)) {
            throw new AlreadyExistsException(ResponseCodeEnum.MOVIE_COLLECTION_ALREADY_EXISTS);
        }
    }

    List<MovieCollection> findByCollection_id(Long collectionId);

    Optional<MovieCollection> findByCollection_idAndMovie_id(Long collectionId, Long movieId);

    default MovieCollection findByCollection_idAndMovie_idOrElseThrow(Long collectionId, Long movieId) {
        return findByCollection_idAndMovie_id(collectionId, movieId).orElseThrow(
                () -> new NotFoundException(ResponseCodeEnum.MOVIE_COLLECTION_NOT_FOUND)
        );
    }
}
