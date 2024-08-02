package com.sparta.filmfly.domain.movie.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sparta.filmfly.domain.movie.entity.Credit;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class ApiCreditsResponseDto {

    @JsonProperty("id")
    private Long id;
    @JsonProperty("adult")
    private boolean adult;
    @JsonProperty("gender")
    private int gender;
    @JsonProperty("known_for_department")
    private String knownForDepartment;
    @JsonProperty("name")
    private String name;
    @JsonProperty("original_name")
    private String originalName;
    @JsonProperty("popularity")
    private double popularity;
    @JsonProperty("profile_path")
    private String profilePath;
    @JsonProperty("cast_id")
    private int castId;
    @JsonProperty("character")
    private String character;
    @JsonProperty("credit_id")
    private String creditId;
    @JsonProperty("order")
    private int order;

    public Credit toEntity() {
        return Credit.builder()
                .id(id)
                .adult(adult)
                .gender(gender)
                .knownForDepartment(knownForDepartment)
                .name(name)
                .originalName(originalName)
                .popularity(popularity)
                .profilePath(profilePath)
                .castId(castId)
                .movieCharacter(character)
                .creditId(creditId)
                .orderNumber(order)
                .build();
    }
}
