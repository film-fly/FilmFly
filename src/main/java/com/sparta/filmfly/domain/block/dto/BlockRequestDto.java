package com.sparta.filmfly.domain.block.dto;

import lombok.Builder;
import lombok.Getter;
import jakarta.validation.constraints.NotNull;

@Builder
@Getter
public class BlockRequestDto {

    @NotNull
    private Long blockedId;

    private String memo;
}
