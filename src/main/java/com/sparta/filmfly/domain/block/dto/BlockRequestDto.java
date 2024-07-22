package com.sparta.filmfly.domain.block.dto;

import lombok.Getter;
import jakarta.validation.constraints.NotNull;

@Getter
public class BlockRequestDto {

    @NotNull
    private Long blockedId;

    private String memo;
}
