package com.sparta.filmfly.domain.user.service;

import com.sparta.filmfly.domain.user.dto.*;
import com.sparta.filmfly.domain.user.entity.User;
import com.sparta.filmfly.domain.user.entity.UserRoleEnum;
import com.sparta.filmfly.domain.user.entity.UserStatusEnum;
import com.sparta.filmfly.domain.user.repository.UserRepository;
import com.sparta.filmfly.global.common.response.ResponseCodeEnum;
import com.sparta.filmfly.global.exception.custom.detail.*;
import com.sparta.filmfly.global.common.S3Uploader;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailVerificationService emailVerificationService;
    private final S3Uploader s3Uploader;

    @Value("${admin_password}")
    private String managerPassword;

    /**
     * 회원가입
     */
    @Transactional
    public UserResponseDto signup(UserSignupRequestDto requestDto) {
        if (userRepository.findByUsername(requestDto.getUsername()).isPresent()) {
            throw new DuplicateException(ResponseCodeEnum.USER_ALREADY_EXISTS);
        }

        if (userRepository.findByEmail(requestDto.getEmail()).isPresent()) {
            throw new DuplicateException(ResponseCodeEnum.EMAIL_ALREADY_EXISTS);
        }

        if (userRepository.findByNickname(requestDto.getNickname()).isPresent()) {
            throw new DuplicateException(ResponseCodeEnum.NICKNAME_ALREADY_EXISTS);
        }

        String encodedPassword = passwordEncoder.encode(requestDto.getPassword());

        UserStatusEnum userStatus = (requestDto.getAdminPassword() != null && !requestDto.getAdminPassword().isEmpty() && managerPassword.equals(requestDto.getAdminPassword()))
                ? UserStatusEnum.VERIFIED
                : UserStatusEnum.UNVERIFIED;

        UserRoleEnum userRole;
        if (requestDto.getAdminPassword() != null && !requestDto.getAdminPassword().isEmpty()) {
            if (!managerPassword.equals(requestDto.getAdminPassword())) {
                throw new InformationMismatchException(ResponseCodeEnum.INVALID_ADMIN_PASSWORD);
            }
            userRole = UserRoleEnum.ROLE_ADMIN;
        } else {
            userRole = UserRoleEnum.ROLE_USER;
        }

        User user = User.builder()
                .username(requestDto.getUsername())
                .password(encodedPassword)
                .email(requestDto.getEmail())
                .nickname(requestDto.getNickname())
                .userStatus(userStatus)
                .userRole(userRole)
                .build();

        userRepository.save(user);

        if (user.getUserRole() != UserRoleEnum.ROLE_ADMIN) {
            emailVerificationService.createVerificationCode(requestDto.getUsername());
        }

        return UserResponseDto.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .nickname(user.getNickname())
                .introduce(user.getIntroduce())
                .pictureUrl(user.getPictureUrl())
                .userRole(user.getUserRole())
                .userStatus(user.getUserStatus())
                .createdAt(user.getCreatedAt())
                .build();
    }

    /**
     * 비밀번호 변경
     */
    @Transactional
    public void updatePassword(User loginUser, UserPasswordUpdateRequestDto requestDto) {
        User user = userRepository.findByIdOrElseThrow(loginUser.getId());

        user.validatePassword(requestDto.getCurrentPassword(), passwordEncoder);
        user.validateNewPassword(requestDto.getNewPassword(), passwordEncoder);

        String encodedNewPassword = passwordEncoder.encode(requestDto.getNewPassword());
        user.updatePassword(encodedNewPassword);
        userRepository.save(user);
    }

    /**
     * 프로필 업데이트
     */
    @Transactional
    public UserResponseDto updateProfile(User user, UserProfileUpdateRequestDto requestDto, MultipartFile profilePicture) {
        // 닉네임 중복 확인
        if (!user.getNickname().equals(requestDto.getNickname())) {
            checkNicknameDuplication(requestDto.getNickname());
        }

        String pictureUrl = user.getPictureUrl();

        if (profilePicture != null && !profilePicture.isEmpty()) {
            try {
                if (!s3Uploader.isFileSame(profilePicture, pictureUrl)) {
                    if (pictureUrl != null && !pictureUrl.isEmpty()) {
                        s3Uploader.delete(pictureUrl); // 기존 프로필 사진 삭제
                        log.info("Old profile picture deleted: {}", pictureUrl);
                    }
                    pictureUrl = s3Uploader.upload(profilePicture, "profile-pictures");
                    log.info("New profile picture uploaded: {}", pictureUrl);
                }
            } catch (IOException e) {
                throw new UploadException(ResponseCodeEnum.UPLOAD_FAILED);
            }
        }

        user.updateProfile(requestDto.getNickname(), requestDto.getIntroduce(), pictureUrl);
        userRepository.save(user);

        return UserResponseDto.builder()
                .id(user.getId())
                .username(user.getUsername())
                .nickname(user.getNickname())
                .introduce(user.getIntroduce())
                .pictureUrl(user.getPictureUrl())
                .updatedAt(user.getUpdatedAt())
                .build();
    }

    /**
     * 닉네임 중복 조회
     */
    @Transactional(readOnly = true)
    public void checkNicknameDuplication(String nickname) {
        if (userRepository.findByNickname(nickname).isPresent()) {
            throw new DuplicateException(ResponseCodeEnum.NICKNAME_ALREADY_EXISTS);
        }
    }

    /**
     * 프로필 조회
     */
    @Transactional(readOnly = true)
    public UserResponseDto getProfile(Long userId) {
        User user = userRepository.findByIdOrElseThrow(userId);
        return UserResponseDto.builder()
                .id(user.getId())
                .nickname(user.getNickname())
                .introduce(user.getIntroduce())
                .pictureUrl(user.getPictureUrl())
                .build();
    }

    /**
     * 로그아웃
     */
    @Transactional
    public void logout(User user) {
        user.deleteRefreshToken();
        userRepository.save(user);
    }

    /**
     * 회원 탈퇴
     */
    @Transactional
    public void deleteUser(User user, UserDeleteRequestDto requestDto) {
        if (!passwordEncoder.matches(requestDto.getPassword(), user.getPassword())) {
            throw new InformationMismatchException(ResponseCodeEnum.PASSWORD_INCORRECT);
        }

        user.deleteRefreshToken();
        user.updateDeleted();
        userRepository.save(user);
    }

    /**
     * 오래된 소프트 딜리트된 유저 삭제
     */
    @Transactional
    public void deleteOldSoftDeletedUsers() {
        LocalDateTime cutoffDate = LocalDateTime.now().minusDays(30); // 30일 지나면 하드 삭제

        // 이메일 인증 삭제
        userRepository.deleteOldSoftDeletedEmailVerifications(cutoffDate);
        // 유저 삭제
        userRepository.deleteOldSoftDeletedUsers(cutoffDate);
    }


    /**
     * 유저 상세 조회 (관리자 기능)
     */
    @Transactional(readOnly = true)
    public UserResponseDto getUserDetail(UserSearchRequestDto userSearchRequestDto) {
        User user = userRepository.findByUsernameOrElseThrow(userSearchRequestDto.getUsername());
        return UserResponseDto.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .nickname(user.getNickname())
                .introduce(user.getIntroduce())
                .pictureUrl(user.getPictureUrl())
                .userRole(user.getUserRole())
                .userStatus(user.getUserStatus())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .deletedAt(user.getDeletedAt())
                .build();
    }

    /**
     * 상태별 유저 조회
     */
    @Transactional(readOnly = true)
    public UserStatusSearchResponseDto getUsersByStatus(UserStatusEnum status) {
        List<User> users = userRepository.findAllByUserStatus(status);

        List<UserResponseDto> userResponseDtos = users.stream()
                .map(user -> UserResponseDto.builder()
                        .id(user.getId())
                        .username(user.getUsername())
                        .deletedAt(user.getDeletedAt())
                        .build())
                .collect(Collectors.toList());

        return UserStatusSearchResponseDto.builder()
                .users(userResponseDtos)
                .userCount(users.size())
                .build();
    }

    /**
     * 유저 정지 시키기
     */
    @Transactional
    public UserResponseDto suspendUser(Long userId) {
        User user = userRepository.findByIdOrElseThrow(userId);

        if (user.getUserRole() != UserRoleEnum.ROLE_USER) {
            throw new InvalidTargetException(ResponseCodeEnum.INVALID_ADMIN_TARGET);
        }
        user.validateDeletedStatus();

        user.updateSuspended();
        userRepository.save(user);

        return UserResponseDto.builder()
                .id(user.getId())
                .username(user.getUsername())
                .userStatus(user.getUserStatus())
                .updatedAt(user.getUpdatedAt())
                .build();
    }



    /**
     * 유저 활성화 상태로 변경
     */
    @Transactional
    public UserResponseDto activateUser(Long userId, User currentUser) {
        User user = userRepository.findByIdOrElseThrow(userId);

        // 어드민이거나 본인 계정(탈퇴 상태인) 경우에만 활성화 가능
        if (currentUser.getUserRole() != UserRoleEnum.ROLE_ADMIN && (!currentUser.getId().equals(userId) || currentUser.getUserStatus() != UserStatusEnum.DELETED)) {
            throw new AccessDeniedException(ResponseCodeEnum.ACCESS_DENIED);
        }

        user.updateVerified();
        userRepository.save(user);

        return UserResponseDto.builder()
                .id(user.getId())
                .username(user.getUsername())
                .userStatus(user.getUserStatus())
                .updatedAt(user.getUpdatedAt())
                .build();
    }

}
