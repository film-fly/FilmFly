package com.sparta.filmfly.domain.comment.repository;

import com.sparta.filmfly.domain.comment.dto.CommentResponseDto;
import java.util.List;

public interface CommentQueryRepository {

    List<CommentResponseDto> findAllExam();
}