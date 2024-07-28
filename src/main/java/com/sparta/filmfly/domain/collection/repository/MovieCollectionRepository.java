package com.sparta.filmfly.domain.collection.repository;

import com.sparta.filmfly.domain.collection.entity.MovieCollection;
import com.sparta.filmfly.global.common.response.ResponseCodeEnum;
import com.sparta.filmfly.global.exception.custom.detail.AlreadyExistsException;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MovieCollectionRepository extends JpaRepository<MovieCollection, Long> {

    boolean existsByCollection_idAndMovie_id(Long collectionId, Long movieId);

    default void existsByCollection_idAndMovie_idOrElseThrow(Long collectionId, Long movieId) {
        if (!existsByCollection_idAndMovie_id(collectionId, movieId)) {
            throw new AlreadyExistsException(ResponseCodeEnum.MOVIE_COLLECTION_ALREADY_EXISTS);
        }
    }

    List<MovieCollection> findByCollection_id(Long collectionId);
}
