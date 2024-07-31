package com.sparta.filmfly.domain.media.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Media {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String s3Url;

    @Column(nullable = false)
    private String fileName;

    @Column(nullable = false)
    private Long size;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private MediaTypeEnum type;

    @Column(nullable = false)
    private Long typeId;

    @Builder
    public Media(String s3Url, String fileName, Long size, MediaTypeEnum type, Long typeId) {
        this.s3Url = s3Url;
        this.fileName = fileName;
        this.size = size;
        this.type = type;
        this.typeId = typeId;
    }
}