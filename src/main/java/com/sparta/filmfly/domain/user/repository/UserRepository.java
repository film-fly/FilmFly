package com.sparta.filmfly.domain.user.repository;

import com.sparta.filmfly.domain.user.entity.User;
import com.sparta.filmfly.domain.user.entity.UserStatusEnum;
import com.sparta.filmfly.global.common.response.ResponseCodeEnum;
import com.sparta.filmfly.global.exception.custom.detail.NotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    default User findByIdOrElseThrow(Long userId) {
        return findById(userId)
                .orElseThrow(() -> new NotFoundException(ResponseCodeEnum.USER_NOT_FOUND));
    }

    Optional<User> findByUsername(String username);

    default User findByUsernameOrElseThrow(String username) {
        return findByUsername(username)
                .orElseThrow(() -> new NotFoundException(ResponseCodeEnum.USER_NOT_FOUND));
    }

    Optional<User> findByEmail(String email);

    List<User> findAllByUserStatus(UserStatusEnum userStatus);

    @Modifying
    @Transactional
    @Query("DELETE FROM User u WHERE u.deletedAt IS NOT NULL AND u.deletedAt < :cutoffDate AND u.userStatus = 'DELETED'")
    void deleteOldSoftDeletedUsers(@Param("cutoffDate") LocalDateTime cutoffDate);

}
