package com.sparta.filmfly.domain.user.service;

import com.sparta.filmfly.domain.board.repository.BoardRepository;
import com.sparta.filmfly.domain.collection.repository.CollectionRepository;
import com.sparta.filmfly.domain.comment.repository.CommentRepository;
import com.sparta.filmfly.domain.favorite.repository.FavoriteRepository;
import com.sparta.filmfly.domain.reaction.repository.BadRepository;
import com.sparta.filmfly.domain.reaction.repository.GoodRepository;
import com.sparta.filmfly.domain.user.dto.*;
import com.sparta.filmfly.domain.user.entity.ContentTypeEnum;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.context.ApplicationContext;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
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
    private final ApplicationContext applicationContext;

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

        if (userRepository.findByNickname(requestDto.getNickname()).isPresent()) {
            throw new DuplicateException(ResponseCodeEnum.NICKNAME_ALREADY_EXISTS);
        }

        String encodedPassword = passwordEncoder.encode(requestDto.getPassword());

        UserRoleEnum userRole;
        if (requestDto.getAdminPassword() != null && !requestDto.getAdminPassword().isEmpty()) {
            if (!managerPassword.equals(requestDto.getAdminPassword())) {
                throw new InformationMismatchException(ResponseCodeEnum.INVALID_ADMIN_PASSWORD);
            }
            userRole = UserRoleEnum.ROLE_ADMIN;
        } else {
            // 이메일 인증 확인 (일반 유저의 경우)
            emailVerificationService.checkIfEmailVerified(requestDto.getEmail());
            userRole = UserRoleEnum.ROLE_USER;
        }

        User user = User.builder()
                .username(requestDto.getUsername())
                .password(encodedPassword)
                .email(requestDto.getEmail())
                .nickname(requestDto.getNickname())
                .userStatus(UserStatusEnum.ACTIVE)
                .userRole(userRole)
                .build();

        userRepository.save(user);

        // 일반 유저일 경우 이메일 인증 데이터 삭제
        if (userRole == UserRoleEnum.ROLE_USER) {
            emailVerificationService.deleteEmailVerificationByEmail(requestDto.getEmail());
        }

        return UserResponseDto.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .nickname(user.getNickname())
                .userStatus(user.getUserStatus())
                .userRole(user.getUserRole())
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
     * 유저 본인 탈퇴 계정 복구
     */
    @Transactional
    public UserResponseDto activateUser(User user) {
        // 활성화 상태 검증
        user.validateActiveStatus();
        user.updateVerified();
        userRepository.save(user);

        return UserResponseDto.builder()
                .id(user.getId())
                .username(user.getUsername())
                .userStatus(user.getUserStatus())
                .updatedAt(user.getUpdatedAt())
                .build();
    }

    /**
     * 오래된 소프트 딜리트된 유저 삭제
     */
    @Transactional
    public void deleteOldSoftDeletedUsers() {
        LocalDateTime cutoffDate = LocalDateTime.now().minusDays(30); // 30일 지나면 하드 삭제

        // 유저 삭제
        userRepository.deleteOldSoftDeletedUsers(cutoffDate);
    }

    /**
     * 유저 상세 조회(관리자 기능)
     */
    @Transactional(readOnly = true)
    public UserResponseDto getUserDetail(Long userId) {
        User user = userRepository.findByIdOrElseThrow(userId);
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
     * 유저 검색 조회(관리자 기능)
     */
    @Transactional(readOnly = true)
    public UserSearchResponseDto getUsersBySearch(String search, UserStatusEnum status, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<User> usersPage;

        if (search != null && !search.isEmpty() && status != null) {
            usersPage = userRepository.findBySearchAndStatus(search, status, pageable);
        } else if (search != null && !search.isEmpty()) {
            usersPage = userRepository.findByUsernameOrNicknameContaining(search, pageable);
        } else if (status != null) {
            usersPage = userRepository.findAllByUserStatus(status, pageable);
        } else {
            usersPage = userRepository.findAll(pageable);
        }

        List<UserResponseDto> userResponseDtos = usersPage.getContent().stream()
                .map(user -> UserResponseDto.builder()
                        .id(user.getId())
                        .username(user.getUsername())
                        .nickname(user.getNickname())
                        .userRole(user.getUserRole())
                        .build())
                .collect(Collectors.toList());

        return UserSearchResponseDto.builder()
                .users(userResponseDtos)
                .userCount(usersPage.getTotalElements())
                .currentPage(usersPage.getNumber())
                .totalPages(usersPage.getTotalPages())
                .build();
    }

    /**
     * 유저 정지 시키기(관리자 기능)
     */
    @Transactional
    public UserResponseDto suspendUser(Long userId) {
        User user = userRepository.findByIdOrElseThrow(userId);
        user.validateDeletedStatus();
        user.validateSuspendedStatus();

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
     * 유저 활성화 시키기(관리자 기능)
     */
    @Transactional
    public UserResponseDto activateUserAsAdmin(Long userId) {
        User user = userRepository.findByIdOrElseThrow(userId);
        user.validateActiveStatus();

        user.updateVerified();
        userRepository.save(user);

        return UserResponseDto.builder()
                .id(user.getId())
                .username(user.getUsername())
                .userStatus(user.getUserStatus())
                .updatedAt(user.getUpdatedAt())
                .build();
    }

    /**
     * 요청 데이터가 본인 데이터인지 응답
     */
    @Transactional(readOnly = true)
    public List<UserOwnerCheckResponseDto> checkOwner(User loginUser, UserOwnerCheckRequestDto requestDto) {
        List<UserOwnerCheckResponseDto> responseDtos = new ArrayList<>();
        ContentTypeEnum contentType = ContentTypeEnum.valueOf(requestDto.getContentType().toUpperCase());

        for (Long contentId : requestDto.getContentIds()) {
            boolean isOwner;

            if (contentType == ContentTypeEnum.USER) {
                isOwner = loginUser.getId().equals(contentId);
            } else {
                isOwner = checkExistAndOwnership(contentType, loginUser.getId(), contentId);
            }

            UserOwnerCheckResponseDto responseDto = UserOwnerCheckResponseDto.builder()
                    .contentType(requestDto.getContentType())
                    .contentId(contentId)
                    .isOwner(isOwner)
                    .build();

            responseDtos.add(responseDto);
        }

        return responseDtos;
    }

    /**
     * 주어진 콘텐츠 타입과 아이디를 기반으로 데이터 확인
     */
    private boolean checkExistAndOwnership(ContentTypeEnum contentType, Long userId, Long contentId) {
        Object repository = applicationContext.getBean(contentType.getRepositoryClass());
        boolean existsAndOwned;

        switch (contentType) {
            case BOARD:
                existsAndOwned = ((BoardRepository) repository).existsByIdAndUserId(contentId, userId);
                break;
            case BAD:
                existsAndOwned = ((BadRepository) repository).existsByIdAndUserId(contentId, userId);
                break;
            case GOOD:
                existsAndOwned = ((GoodRepository) repository).existsByIdAndUserId(contentId, userId);
                break;
            case COMMENT:
                existsAndOwned = ((CommentRepository) repository).existsByIdAndUserId(contentId, userId);
                break;
            case FAVORITE:
                existsAndOwned = ((FavoriteRepository) repository).existsByIdAndUserId(contentId, userId);
                break;
            case COLLECTION:
                existsAndOwned = ((CollectionRepository) repository).existsByIdAndUserId(contentId, userId);
                break;
            default:
                throw new IllegalArgumentException("Unknown content type: " + contentType);
        }

        return existsAndOwned;
    }

}
