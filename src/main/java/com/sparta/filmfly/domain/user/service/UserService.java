package com.sparta.filmfly.domain.user.service;

import com.sparta.filmfly.domain.user.dto.PasswordUpdateRequestDto;
import com.sparta.filmfly.domain.user.dto.SignupRequestDto;
import com.sparta.filmfly.domain.user.dto.UserResponseDto;
import com.sparta.filmfly.domain.user.entity.User;
import com.sparta.filmfly.domain.user.entity.UserRoleEnum;
import com.sparta.filmfly.domain.user.entity.UserStatusEnum;
import com.sparta.filmfly.domain.user.repository.UserRepository;
import com.sparta.filmfly.global.common.response.ResponseCodeEnum;
import com.sparta.filmfly.global.exception.custom.detail.DuplicateException;
import com.sparta.filmfly.global.exception.custom.detail.InformationMismatchException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailVerificationService emailVerificationService;

    @Value("${admin_password}")
    private String managerPassword;

    @Transactional
    public UserResponseDto signup(SignupRequestDto requestDto) {
        // 중복된 사용자 체크
        if (userRepository.findByUsername(requestDto.getUsername()).isPresent()) {
            throw new DuplicateException(ResponseCodeEnum.USER_ALREADY_EXISTS);
        }

        // 비밀번호 인코딩
        String encodedPassword = passwordEncoder.encode(requestDto.getPassword());

        // User 엔티티 생성
        User user = User.builder()
                .username(requestDto.getUsername())
                .password(encodedPassword)
                .email(requestDto.getEmail())
                .userStatus(determineUserStatus(requestDto.getAdminPassword())) // 유저 상태 결정
                .userRole(determineUserRole(requestDto.getAdminPassword())) // 유저 역할 결정
                .build();

        // User 엔티티 저장
        userRepository.save(user);

        // 어드민이 아닌 경우에만 이메일 인증 코드 전송
        if (user.getUserRole() != UserRoleEnum.ROLE_ADMIN) {
            emailVerificationService.createVerificationCode(requestDto.getUsername());
        }

        // UserResponseDto 반환
        return UserResponseDto.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .userRole(user.getUserRole())
                .userStatus(user.getUserStatus())
                .build();
    }

    // 비밀번호 변경
    @Transactional
    public void updatePassword(User loginUser, PasswordUpdateRequestDto requestDto) {
        User user = userRepository.findByIdOrElseThrow(loginUser.getId());

        user.validatePassword(requestDto.getCurrentPassword(), passwordEncoder);
        user.validateNewPassword(requestDto.getNewPassword(), passwordEncoder);

        String encodedNewPassword = passwordEncoder.encode(requestDto.getNewPassword());
        user.updatePassword(encodedNewPassword);
        userRepository.save(user);
    }

    // 유저 상태 설정
    private UserStatusEnum determineUserStatus(String adminPassword) {
        return (adminPassword != null && !adminPassword.isEmpty() && managerPassword.equals(adminPassword))
                ? UserStatusEnum.VERIFIED
                : UserStatusEnum.UNVERIFIED;
    }

    // 유저 권한 설정
    private UserRoleEnum determineUserRole(String adminPassword) {
        if (adminPassword != null && !adminPassword.isEmpty()) {
            if (!managerPassword.equals(adminPassword)) {
                throw new InformationMismatchException(ResponseCodeEnum.INVALID_ADMIN_PASSWORD);
            }
            return UserRoleEnum.ROLE_ADMIN;
        }
        return UserRoleEnum.ROLE_USER;
    }
}
