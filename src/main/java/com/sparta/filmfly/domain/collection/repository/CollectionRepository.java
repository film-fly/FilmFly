package com.sparta.filmfly.domain.collection.repository;

import com.sparta.filmfly.domain.collection.entity.Collection;
import com.sparta.filmfly.domain.user.entity.User;
import com.sparta.filmfly.global.common.response.ResponseCodeEnum;
import com.sparta.filmfly.global.exception.custom.detail.AlreadyExistsException;
import com.sparta.filmfly.global.exception.custom.detail.NotFoundException;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CollectionRepository extends JpaRepository<Collection, Long> {
    boolean existsByUser_IdAndName(Long userId, String name);

    List<Collection> findAllByUser(User user);

    default void existsByUser_IdAndNameOrElseThrow(Long userId, String name) {
        if(existsByUser_IdAndName(userId, name)) {
            throw new AlreadyExistsException(ResponseCodeEnum.COLLECTION_ALREADY_EXISTS);
        }
    }

    default Collection findByIdOrElseThrow(Long collectionId) {
        return findById(collectionId).orElseThrow(
                () -> new NotFoundException(ResponseCodeEnum.COLLECTION_NOT_FOUND)
        );
    }

    boolean existsByIdAndUserId(Long id, Long userId);

}