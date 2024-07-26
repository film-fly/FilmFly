package com.sparta.filmfly.domain.collection.entity;

import com.sparta.filmfly.domain.movie.entity.Movie;
import com.sparta.filmfly.domain.user.entity.User;
import com.sparta.filmfly.global.common.TimeStampEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MovieCollection extends TimeStampEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(nullable = true, name = "collection_id")
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Collection collection;

    @JoinColumn(nullable = true, name = "movie_id")
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Movie movie;

    @Builder
    public MovieCollection(Collection collection, Movie movie) {
        this.collection = collection;
        this.movie = movie;
    }
}
