package com.sparta.filmfly.domain.block.repository;

import com.sparta.filmfly.domain.block.entity.Block;
import com.sparta.filmfly.domain.user.entity.User;
import com.sparta.filmfly.global.common.response.ResponseCodeEnum;
import com.sparta.filmfly.global.exception.custom.detail.DuplicateException;
import com.sparta.filmfly.global.exception.custom.detail.InvalidTargetException;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BlockRepository extends JpaRepository<Block, Long> {
    Optional<Block> findByBlockerAndBlocked(User blocker, User blocked);

    default void checkIfAlreadyBlocked(User blocker, User blocked) {
        findByBlockerAndBlocked(blocker, blocked)
                .ifPresent(block -> {
                    throw new DuplicateException(ResponseCodeEnum.USER_ALREADY_BLOCKED);
                });
    }

    default Block findByBlockerAndBlockedOrElseThrow(User blocker, User blocked) {
        return findByBlockerAndBlocked(blocker, blocked)
                .orElseThrow(() -> new InvalidTargetException(ResponseCodeEnum.NOT_BLOCKED_TARGET));
    }

    List<Block> findByBlocker(User blocker);
}
