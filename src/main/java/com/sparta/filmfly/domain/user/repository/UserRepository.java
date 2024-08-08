package com.sparta.filmfly.domain.user.repository;

import com.sparta.filmfly.domain.user.entity.User;
import com.sparta.filmfly.domain.user.entity.UserStatusEnum;
import com.sparta.filmfly.global.common.response.ResponseCodeEnum;
import com.sparta.filmfly.global.exception.custom.detail.NotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
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

    Optional<User> findByNickname(String nickname);

    @Query("SELECT u FROM User u WHERE (:search IS NULL OR u.username LIKE %:search% OR u.nickname LIKE %:search%) AND (:status IS NULL OR u.userStatus = :status)")
    Page<User> findBySearchAndStatus(@Param("search") String search, @Param("status") UserStatusEnum status, Pageable pageable);

    @Query("SELECT u FROM User u WHERE u.username LIKE %:search% OR u.nickname LIKE %:search%")
    Page<User> findByUsernameOrNicknameContaining(@Param("search") String search, Pageable pageable);

    Page<User> findAllByUserStatus(UserStatusEnum userStatus, Pageable pageable);

    Page<User> findAll(Pageable pageable);

    @Modifying
    @Transactional
    @Query("DELETE FROM User u WHERE u.deletedAt IS NOT NULL AND u.deletedAt < :cutoffDate AND u.userStatus = 'DELETED'")
    void deleteOldSoftDeletedUsers(@Param("cutoffDate") LocalDateTime cutoffDate);

    boolean existsByUsername(String username);

    boolean existsByNickname(String nickname);

    boolean existsByEmail(String email);

    long count();
}