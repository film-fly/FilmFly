package com.sparta.filmfly.domain.user.service;

import com.sparta.filmfly.domain.user.dto.PasswordUpdateRequestDto;
import com.sparta.filmfly.domain.user.dto.ProfileUpdateRequestDto;
import com.sparta.filmfly.domain.user.dto.SignupRequestDto;
import com.sparta.filmfly.domain.user.dto.UserResponseDto;
import com.sparta.filmfly.domain.user.entity.User;
import com.sparta.filmfly.domain.user.entity.UserRoleEnum;
import com.sparta.filmfly.domain.user.entity.UserStatusEnum;
import com.sparta.filmfly.domain.user.repository.UserRepository;
import com.sparta.filmfly.global.common.response.ResponseCodeEnum;
import com.sparta.filmfly.global.exception.custom.detail.DuplicateException;
import com.sparta.filmfly.global.exception.custom.detail.InformationMismatchException;
import com.sparta.filmfly.global.exception.custom.detail.UploadException;
import com.sparta.filmfly.global.common.S3Uploader;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailVerificationService emailVerificationService;
    private final S3Uploader s3Uploader;

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
                .nickname(user.getNickname())
                .introduce(user.getIntroduce())
                .pictureUrl(user.getPictureUrl())
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

    // 프로필 업데이트
    @Transactional
    public void updateProfile(User user, ProfileUpdateRequestDto requestDto, MultipartFile profilePicture) {
        String pictureUrl = user.getPictureUrl(); // 기존 URL 유지

        if (profilePicture != null && !profilePicture.isEmpty()) {
            try {
                pictureUrl = s3Uploader.upload(profilePicture, "profile-pictures");
            } catch (IOException e) {
                throw new UploadException(ResponseCodeEnum.UPLOAD_FAILED);
            }
        }

        user.updateProfile(requestDto.getNickname(), requestDto.getIntroduce(), pictureUrl);
        userRepository.save(user);
    }

    // 프로필 조회
    @Transactional(readOnly = true)
    public UserResponseDto getProfile(Long userId) {
        User user = userRepository.findByIdOrElseThrow(userId);
        return UserResponseDto.builder()
                .nickname(user.getNickname())
                .introduce(user.getIntroduce())
                .pictureUrl(user.getPictureUrl())
                .build();
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
