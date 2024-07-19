package com.sparta.filmfly.domain.review.repository;

import com.sparta.filmfly.domain.review.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review, Long> {

}