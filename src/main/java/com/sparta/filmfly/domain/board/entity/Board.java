package com.sparta.filmfly.domain.board.entity;

import com.sparta.filmfly.domain.exam.dto.ExamUpdateRequestDto;
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
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;

@Entity
@Getter
@SQLDelete(sql = "UPDATE board SET deleted_at = CURRENT_TIMESTAMP where id = ?")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Board extends TimeStampEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private String title;


    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private Long goodCount;

    @Column(nullable = false)
    private Long badCount;

    @Column(nullable = false)
    private Long hits;

    @Builder // 필요한 것만 생성자로
    public Board(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public void update(ExamUpdateRequestDto requestDto) {
        this.title = requestDto.getTitle() != null ? requestDto.getTitle() : title;
        this.content = requestDto.getContent() != null ? requestDto.getContent() : content;
    }

    public void validateExam() {
        // exam 검증
        // ex) 사용자가 탈퇴 여부 검증
    }
}