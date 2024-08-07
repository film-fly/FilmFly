package com.sparta.filmfly.domain.movie.dto;

import com.sparta.filmfly.domain.movie.entity.LanguagesEnum;
import com.sparta.filmfly.domain.movie.entity.SortByEnum;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.aspectj.lang.annotation.RequiredTypes;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class ApiSearchMovieRequestDto {
    @NotBlank
    private String query;
    private boolean includeAdult;
    private LanguagesEnum language;
    private String primary_release_year;
    private int page;
    private String region;
    private String year;
    // 기본 생성자 default 값 설정
    public ApiSearchMovieRequestDto() {
        this.includeAdult = false;
        this.language = LanguagesEnum.KO_KR;
        this.page = 1;
    }
}
