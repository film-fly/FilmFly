package com.sparta.spring_init_template.global.common.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MessageResponseDto {
    private Integer statusCode;
    private String message;
}