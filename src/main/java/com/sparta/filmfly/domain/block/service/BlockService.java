package com.sparta.filmfly.domain.block.service;

import com.sparta.filmfly.domain.block.dto.BlockedUserResponseDto;
import com.sparta.filmfly.domain.block.entity.Block;
import com.sparta.filmfly.domain.block.repository.BlockRepository;
import com.sparta.filmfly.domain.user.dto.UserResponseDto;
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

    // 차단하 기
    @Transactional
    public void blockUser(Long blockerId, Long blockedId, String memo) {
        User blocker = userRepository.findByIdOrElseThrow(blockerId);
        User blocked = userRepository.findByIdOrElseThrow(blockedId);

        // 이미 차단된 관계가 있는지 확인
        blockRepository.checkIfAlreadyBlocked(blocker, blocked);

        Block block = Block.builder()
                .blocker(blocker)
                .blocked(blocked)
                .memo(memo)
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
                        .build())
                .collect(Collectors.toList());
    }
}
