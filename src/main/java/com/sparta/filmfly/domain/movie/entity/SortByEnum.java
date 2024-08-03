package com.sparta.filmfly.domain.movie.entity;

import lombok.Getter;

@Getter
public enum SortByEnum{
    ORIGINAL_TITLE_ASC("original_title.asc"),
    ORIGINAL_TITLE_DESC("original_title.desc"),
    POPULARITY_ASC("popularity.asc"),
    POPULARITY_DESC("popularity.desc"),
    REVENUE_ASC("revenue.asc"),
    REVENUE_DESC("revenue.desc"),
    PRIMARY_RELEASE_DATE_ASC("primary_release_date.asc"),
    PRIMARY_RELEASE_DATE_DESC("primary_release_date.desc"),
    TITLE_ASC("title.asc"),
    TITLE_DESC("title.desc"),
    VOTE_AVERAGE_ASC("vote_average.asc"),
    VOTE_AVERAGE_DESC("vote_average.desc"),
    VOTE_COUNT_ASC("vote_count.asc"),
    VOTE_COUNT_DESC("vote_count.desc");

    private final String value;

    SortByEnum(String value) {
        this.value = value;
    }
}