package com.sparta.filmfly.domain.movie.dto;

import com.sparta.filmfly.domain.movie.entity.Credit;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CreditResponseDto {
    private Long id;
    private int gender;
    private String knownForDepartment;
    private String name;
    private String originalName;
    private String profilePath;
    private int castId; // 캐스팅 순서
    private String movieCharacter;
    private String creditId;    // 역할 id
    private int orderNumber;

    public static CreditResponseDto fromEntity(Credit credit) {
        return CreditResponseDto.builder()
                .id(credit.getId())
                .gender(credit.getGender())
                .knownForDepartment(credit.getKnownForDepartment())
                .name(credit.getName())
                .originalName(credit.getOriginalName())
                .profilePath(credit.getProfilePath())
                .castId(credit.getCastId())
                .movieCharacter(credit.getMovieCharacter())
                .creditId(credit.getCreditId())
                .orderNumber(credit.getOrderNumber())
                .build();
    }
}
