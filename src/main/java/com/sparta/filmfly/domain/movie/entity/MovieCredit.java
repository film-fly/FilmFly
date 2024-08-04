package com.sparta.filmfly.domain.movie.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class MovieCredit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "movie_id")
    private Movie movie;

    @ManyToOne
    @JoinColumn(name = "credit_id")
    private Credit credit;

    public void updateMovie(Movie movie) {
        this.movie = movie;
    }
    public void updateCredit(Credit credit) {
        this.credit = credit;
    }
}