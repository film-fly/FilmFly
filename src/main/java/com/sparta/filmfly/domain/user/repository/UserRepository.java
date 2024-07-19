package com.sparta.filmfly.domain.user.repository;

import com.sparta.filmfly.domain.user.entity.User;
import com.sparta.filmfly.global.common.response.ResponseCodeEnum;
import com.sparta.filmfly.global.exception.custom.detail.NotFoundException;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

    default User findByIdOrElseThrow(Long userId) {
        return findById(userId)
            .orElseThrow(() -> new NotFoundException(ResponseCodeEnum.USER_NOT_FOUND));
    }
}