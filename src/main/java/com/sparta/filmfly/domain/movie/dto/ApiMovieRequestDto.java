package com.sparta.filmfly.domain.movie.dto;

import com.sparta.filmfly.domain.movie.entity.LanguagesEnum;
import com.sparta.filmfly.domain.movie.entity.OriginLanguageEnum;
import com.sparta.filmfly.domain.movie.entity.SortByEnum;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class ApiMovieRequestDto {
    private String certification;
    private String certification__gte;
    private String certification__lte;
    private String certification_country;
    private boolean include_adult;  // 기본 값 설정
    private boolean include_video;  // 기본 값 설정
    private LanguagesEnum language;  // 기본 값 설정
    private int page;  // 기본 값 설정
    private int primary_release_year;
    private LocalDate primary_release_date__gte;
    private LocalDate primary_release_date__lte;
    private String region;
    private LocalDate release_date__gte;
    private LocalDate release_date__lte;
    private SortByEnum sort_by;  // 기본 값 설정
    private float vote_average__gte;
    private float vote_average__lte;
    private float vote_count__gte;
    private float vote_count__lte;
    private String watch_region;
    private String with_cast;
    private String with_companies;
    private String with_crew;
    private String with_genres;
    private String with_keywords;
    private String with_origin_country;
    private OriginLanguageEnum with_original_language;
    private String with_people;
    private int with_release_type;
    private int with_runtime__gte;
    private int with_runtime__lte;
    private String with_watch_monetization_types;
    private String with_watch_providers;
    private String without_companies;
    private String without_genres;
    private String without_keywords;
    private String without_watch_providers;
    private int year;

    // 기본 생성자 default 값 설정
    public ApiMovieRequestDto() {
        this.include_adult = false;
        this.include_video = false;
        this.language = LanguagesEnum.KO_KR;
        this.page = 1;
        this.sort_by = SortByEnum.POPULARITY_DESC;
    }
}
