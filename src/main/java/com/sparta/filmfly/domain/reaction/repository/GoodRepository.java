package com.sparta.filmfly.domain.reaction.repository;

import com.sparta.filmfly.domain.reaction.ReactionContentTypeEnum;
import com.sparta.filmfly.domain.reaction.entity.Good;
import com.sparta.filmfly.global.common.response.ResponseCodeEnum;
import com.sparta.filmfly.global.exception.custom.detail.AlreadyActionException;
import com.sparta.filmfly.global.exception.custom.detail.NotFoundException;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface GoodRepository extends JpaRepository<Good, Long> {

    Optional<Good> findByTypeIdAndTypeAndUserId(Long typeId, ReactionContentTypeEnum type, Long userId);
    default Good findByTypeIdAndTypeAndUserIdOrElseThrow(Long typeId, ReactionContentTypeEnum type, Long userId) {
        return this.findByTypeIdAndTypeAndUserId(typeId, type, userId)
                .orElseThrow(() -> new AlreadyActionException(ResponseCodeEnum.GOOD_ALREADY_REMOVE));
    }

    boolean existsByTypeIdAndTypeAndUserId(Long contentId, ReactionContentTypeEnum type, Long userId);
    default void existsByTypeIdAndTypeAndUserIdOrElseThrow(Long contentId, ReactionContentTypeEnum type, Long userId) {
        if (this.existsByTypeIdAndTypeAndUserId(contentId, type, userId)) {
            throw new NotFoundException(ResponseCodeEnum.GOOD_ALREADY_ADD);
        }
    }

    Long countByTypeAndTypeId(ReactionContentTypeEnum type, Long typeId);
}