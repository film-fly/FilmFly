package com.sparta.filmfly.domain.officeboard.entity;

import com.sparta.filmfly.domain.officeboard.dto.OfficeBoardRequestDto;
import com.sparta.filmfly.domain.user.entity.User;
import com.sparta.filmfly.global.common.TimeStampEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SQLDelete(sql = "UPDATE office_board SET deleted_at = CURRENT_TIMESTAMP where id = ?")
public class OfficeBoard extends TimeStampEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    String title;

    @Column(nullable = false)
    String content;

    @Column(nullable = false)
    Long hits;

    @Column(nullable = false)
    Long goodCount;

    @Builder
    public OfficeBoard(String title, String content){
        this.title = title;
        this.content = content;
    }

    public void update(OfficeBoardRequestDto requestDto) {
        this.title = requestDto.getTitle() != null ? requestDto.getTitle() : title;
        this.content = requestDto.getContent() != null ? requestDto.getContent() : content;
    }
}