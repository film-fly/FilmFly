package com.sparta.filmfly.domain.comment.repository;

import com.sparta.filmfly.domain.comment.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long>, CommentQueryRepository {
}