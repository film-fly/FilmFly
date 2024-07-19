package com.sparta.filmfly.domain.reaction.repository;

import com.sparta.filmfly.domain.reaction.entity.Good;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GoodRepository extends JpaRepository<Good, Long> {

}