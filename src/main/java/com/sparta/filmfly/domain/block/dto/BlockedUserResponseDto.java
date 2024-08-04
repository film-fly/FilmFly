package com.sparta.filmfly.domain.block.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class BlockedUserResponseDto {
    private Long id;
    private String username;
    private String nickname;
    private String memo;
    private LocalDateTime createdAt;
}
