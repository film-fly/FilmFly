package com.sparta.filmfly.domain.user.repository;

import com.sparta.filmfly.domain.user.entity.User;
import com.sparta.filmfly.domain.user.entity.UserStatusEnum;
import com.sparta.filmfly.global.common.response.ResponseCodeEnum;
import com.sparta.filmfly.global.exception.custom.detail.NotFoundException;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    // 사용자 ID로 유저를 찾고 없으면 예외 발생
    default User findByIdOrElseThrow(Long userId) {
        return findById(userId)
                .orElseThrow(() -> new NotFoundException(ResponseCodeEnum.USER_NOT_FOUND));
    }

    // 사용자 이름으로 유저를 찾기
    Optional<User> findByUsername(String username);

    // 사용자 이름으로 유저를 찾고 없으면 예외 발생
    default User findByUsernameOrElseThrow(String username) {
        return findByUsername(username)
                .orElseThrow(() -> new NotFoundException(ResponseCodeEnum.USER_NOT_FOUND));
    }

    // 상태별 유저 조회
    List<User> findAllByUserStatus(UserStatusEnum userStatus);

}
