package com.sparta.filmfly.domain.block.service;

import com.sparta.filmfly.domain.block.dto.BlockedUserPageResponseDto;
import com.sparta.filmfly.domain.block.dto.BlockedUserResponseDto;
import com.sparta.filmfly.domain.block.dto.BlockRequestDto;
import com.sparta.filmfly.domain.block.entity.Block;
import com.sparta.filmfly.domain.block.repository.BlockRepository;
import com.sparta.filmfly.domain.user.entity.User;
import com.sparta.filmfly.domain.user.repository.UserRepository;
import com.sparta.filmfly.global.common.response.ResponseCodeEnum;
import com.sparta.filmfly.global.exception.custom.detail.DuplicateException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BlockService {

    private final BlockRepository blockRepository;
    private final UserRepository userRepository;

    /**
     * 차단 하기
     */
    @Transactional
    public void blockUser(Long blockerId, BlockRequestDto blockRequestDto) {
        User blocker = userRepository.findByIdOrElseThrow(blockerId);
        User blocked = userRepository.findByIdOrElseThrow(blockRequestDto.getBlockedId());

        // 이미 차단된 관계가 있는지 확인
        if (blockRepository.existsByBlockerAndBlocked(blocker, blocked)) {
            throw new DuplicateException(ResponseCodeEnum.USER_ALREADY_BLOCKED);
        }

        // 본인 차단 여부 확인
        if (blocker.equals(blocked)) {
            throw new DuplicateException(ResponseCodeEnum.INVALID_SELF_TARGET);
        }


        Block block = Block.builder()
                .blocker(blocker)
                .blocked(blocked)
                .memo(blockRequestDto.getMemo())
                .build();
        blockRepository.save(block);
    }

    /**
     * 내가 차단한 유저 목록 조회
     */
    @Transactional(readOnly = true)
    public BlockedUserPageResponseDto getBlockedUsers(User blocker, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Block> blockedUsersPage = blockRepository.findByBlocker(blocker, pageable);

        List<BlockedUserResponseDto> blockedUserDtos = blockedUsersPage.getContent().stream()
                .map(block -> BlockedUserResponseDto.builder()
                        .id(block.getBlocked().getId())
                        .username(block.getBlocked().getUsername())
                        .nickname(block.getBlocked().getNickname())
                        .memo(block.getMemo())
                        .createdAt(block.getCreatedAt())
                        .build())
                .collect(Collectors.toList());

        return BlockedUserPageResponseDto.builder()
                .users(blockedUserDtos)
                .userCount(blockedUsersPage.getTotalElements())
                .currentPage(blockedUsersPage.getNumber())
                .totalPages(blockedUsersPage.getTotalPages())
                .build();
    }

    /**
     * 차단 해제
     */
    @Transactional
    public void unblockUser(Long blockerId, Long blockedId) {
        User blocker = userRepository.findByIdOrElseThrow(blockerId);
        User blocked = userRepository.findByIdOrElseThrow(blockedId);

        Block block = blockRepository.findByBlockerAndBlockedOrElseThrow(blocker, blocked);

        blockRepository.delete(block);
    }
}
