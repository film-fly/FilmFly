package com.sparta.filmfly.domain.movie.dto;

import java.time.LocalDate;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@ToString
public class MovieSearchCond {

    private String keyword;
    private List<Integer> genreIds;
    private List<Integer> adults;
    private LocalDate releaseDateFrom;
    private LocalDate releaseDateTo;

    public static MovieSearchCond createSearchCondition(
        String search,
        List<Integer> genreIds,
        List<Integer> adults,
        LocalDate releaseDateFrom,
        LocalDate releaseDateTo
    ) {
        return MovieSearchCond.builder()
            .keyword(search)
            .genreIds(genreIds == null ? List.of() : genreIds)
            .adults(adults == null ? List.of() : adults)
            .releaseDateFrom(releaseDateFrom)
            .releaseDateTo(releaseDateTo)
            .build();
    }
}