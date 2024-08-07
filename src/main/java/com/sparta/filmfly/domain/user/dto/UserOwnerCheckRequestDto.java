package com.sparta.filmfly.domain.user.dto;

import lombok.Getter;


import java.util.List;

@Getter
public class UserOwnerCheckRequestDto {
    private String contentType;
    private List<Long> contentIds;
}
