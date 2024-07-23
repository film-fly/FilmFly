package com.sparta.filmfly.domain.block.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class BlockedUserResponseDto {
    private Long id;
    private String username;
    private String nickname;
    private String memo;
}
