package com.sparta.filmfly.domain.block.service;

import com.sparta.filmfly.domain.block.dto.BlockedUserResponseDto;
import com.sparta.filmfly.domain.block.dto.BlockRequestDto;
import com.sparta.filmfly.domain.block.entity.Block;
import com.sparta.filmfly.domain.block.repository.BlockRepository;
import com.sparta.filmfly.domain.user.entity.User;
import com.sparta.filmfly.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BlockService {

    private final BlockRepository blockRepository;
    private final UserRepository userRepository;

    // 차단 하기
    @Transactional
    public void blockUser(Long blockerId, BlockRequestDto blockRequestDto) {
        User blocker = userRepository.findByIdOrElseThrow(blockerId);
        User blocked = userRepository.findByIdOrElseThrow(blockRequestDto.getBlockedId());

        // 이미 차단된 관계가 있는지 확인
        blockRepository.checkIfAlreadyBlocked(blocker, blocked);

        Block block = Block.builder()
                .blocker(blocker)
                .blocked(blocked)
                .memo(blockRequestDto.getMemo())
                .build();
        blockRepository.save(block);
    }

    // 내가 차단한 유저 목록 조회
    @Transactional(readOnly = true)
    public List<BlockedUserResponseDto> getBlockedUsers(User blocker) {
        List<Block> blockedUsers = blockRepository.findByBlocker(blocker);
        return blockedUsers.stream()
                .map(block -> BlockedUserResponseDto.builder()
                        .id(block.getBlocked().getId())
                        .username(block.getBlocked().getUsername())
                        .nickname(block.getBlocked().getNickname())
                        .memo(block.getMemo())
                        .createdAt(block.getCreatedAt())
                        .build())
                .collect(Collectors.toList());
    }

    // 차단 해제
    @Transactional
    public void unblockUser(Long blockerId, Long blockedId) {
        User blocker = userRepository.findByIdOrElseThrow(blockerId);
        User blocked = userRepository.findByIdOrElseThrow(blockedId);

        // 차단한 상대인지 확인
        Block block = blockRepository.findByBlockerAndBlockedOrElseThrow(blocker, blocked);

        blockRepository.delete(block);
    }
}
