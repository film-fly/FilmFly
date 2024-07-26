package com.sparta.filmfly.domain.collection.entity;

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

    @Column(nullable = false)
    private String name;

    private String content;

    @Builder
    public MovieCollection(String name, String content) {
        this.name = name;
        this.content = content;
    }
}
