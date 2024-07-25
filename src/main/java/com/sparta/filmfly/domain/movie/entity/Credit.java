package com.sparta.filmfly.domain.movie.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class Credit {
    @Id
    private Long id;
    private boolean adult;
    private int gender;
    private Long actorId;
    private String knownForDepartment;
    private String name;
    private String originalName;
    private double popularity;
    private String profilePath;
    private int castId;
    private String movieCharacter;
    private String creditId;
    private int orderNumber;
    @OneToMany(mappedBy = "credit", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<MovieCredit> movieCreditList;

    public void updateMovieCreditList(List<MovieCredit> movieCreditList) {
        this.movieCreditList = movieCreditList;
    }
}