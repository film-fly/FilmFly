package com.sparta.filmfly.domain.exam.entity;

import com.sparta.filmfly.domain.exam.dto.ExamResponseDto;
import com.sparta.filmfly.global.common.TimeStampEntity;
import com.sparta.filmfly.domain.exam.dto.ExamUpdateRequestDto;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Exam extends TimeStampEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // @Column(nullable = false) 붙이기
//    @NotBlank + Validate 로직 추가해서 생기는 이득, 손해 -> 가독성 손해, DB단 까지 안가도 된다
//    DB nullable 을 해서 안에 들어가서 null 체크 했을 때 생기는 이득, 손해 -> 가독성 이득, DB단 에서 체크를 한다
    @Column(nullable = false)
    private String title;
    @Column(nullable = false)
    private String content;

    @Builder // 필요한 것만 생성자로
    public Exam(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public ExamResponseDto toExamResponseDto() {
        return ExamResponseDto.builder()
            .title(this.title)
            .content(this.content)
            .build();
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