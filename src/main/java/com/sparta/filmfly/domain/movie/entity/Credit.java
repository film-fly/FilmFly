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

/*
개인정보
{
  "adult": false,
  "also_known_as": [
    "Джордж Орсон Уэллс",
    "George Orson Welles",
    "O. W. Jeeves",
    "오슨 웰스",
    "오슨 웰즈",
    "오손 웰스",
    "اورسون ولز"
  ],
  "biography": "George Orson Welles (May 6, 1915 – October 10, 1985), best known as Orson Welles, was an American film director, actor, theatre director, screenwriter, and producer, who worked extensively in film, theatre, television and radio. Noted for his innovative dramatic productions as well as his distinctive voice and personality, Welles is widely acknowledged as one of the most accomplished dramatic artists of the twentieth century, especially for his significant and influential early work—despite his notoriously contentious relationship with Hollywood. His distinctive directorial style featured layered, nonlinear narrative forms, innovative uses of lighting such as chiaroscuro, unique camera angles, sound techniques borrowed from radio, deep focus shots, and long takes. Welles's long career in film is noted for his struggle for artistic control in the face of pressure from studios. Many of his films were heavily edited and others left unreleased. He has been praised as a major creative force and as \"the ultimate auteur.\"\n\nAfter directing a number of high-profile theatrical productions in his early twenties, including an innovative adaptation of Macbeth and The Cradle Will Rock, Welles found national and international fame as the director and narrator of a 1938 radio adaptation of H. G. Wells's novel The War of the Worlds performed for the radio drama anthology series Mercury Theatre on the Air. It was reported to have caused widespread panic when listeners thought that an invasion by extraterrestrial beings was occurring. Although these reports of panic were mostly false and overstated, they rocketed Welles to instant notoriety.\n\nCitizen Kane (1941), his first film with RKO, in which he starred in the role of Charles Foster Kane, is often considered the greatest film ever made. Several of his other films, including The Magnificent Ambersons (1942), The Lady from Shanghai (1947), Touch of Evil (1958), Chimes at Midnight (1965), and F for Fake (1974), are also widely considered to be masterpieces.\n\nIn 2002, he was voted the greatest film director of all time in two separate British Film Institute polls among directors and critics, and a wide survey of critical consensus, best-of lists, and historical retrospectives calls him the most acclaimed director of all time. Andrew Sarris in his influential book of film criticism The American Cinema: Directors and Directions 1929–1968 included him in the \"pantheon\" of the 14 greatest film directors who had worked in the United States. Well known for his baritone voice, Welles was also an extremely well regarded actor and was voted number 16 in AFI's 100 Years... 100 Stars list of the greatest American film actors of all time. He was also a celebrated Shakespearean stage actor and an accomplished magician, starring in troop variety shows in the war years.",
  "birthday": "1915-05-06",
  "deathday": "1985-10-10",
  "gender": 2,
  "homepage": null,
  "id": 40,
  "imdb_id": "nm0000080",
  "known_for_department": "Directing",
  "name": "Orson Welles",
  "place_of_birth": "Kenosha, Wisconsin, USA",
  "popularity": 14.553,
  "profile_path": "/e9lGmqQq3EsHeUQgQLByo275hcc.jpg"
}
*/