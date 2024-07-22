package com.sparta.filmfly.domain.block.service;

import com.sparta.filmfly.domain.block.entity.Block;
import com.sparta.filmfly.domain.block.repository.BlockRepository;
import com.sparta.filmfly.domain.user.entity.User;
import com.sparta.filmfly.domain.user.repository.UserRepository;
import com.sparta.filmfly.global.common.response.ResponseCodeEnum;
import com.sparta.filmfly.global.exception.custom.detail.DuplicateException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BlockService {

    private final BlockRepository blockRepository;
    private final UserRepository userRepository;

    // 유저 차단 기능
    @Transactional
    public void blockUser(Long blockerId, Long blockedId, String memo) {

        User blocker = userRepository.findByIdOrElseThrow(blockerId);
        User blocked = userRepository.findByIdOrElseThrow(blockedId);

        // 이미 차단된 관계가 있는지 확인
        blockRepository.findByBlockerAndBlocked(blocker, blocked)
                .ifPresent(block -> {
                    throw new DuplicateException(ResponseCodeEnum.USER_ALREADY_BLOCKED);
                });

        Block block = Block.builder()
                .blocker(blocker)
                .blocked(blocked)
                .memo(memo)
                .build();

        blockRepository.save(block);
    }
}
