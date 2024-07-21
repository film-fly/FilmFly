package com.sparta.filmfly.domain.reaction.repository;

import com.sparta.filmfly.domain.reaction.ReactionContentTypeEnum;
import com.sparta.filmfly.domain.reaction.entity.Bad;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BadRepository extends JpaRepository<Bad, Long> {

    Optional<Bad> findByTypeIdAndType(Long contentId, ReactionContentTypeEnum contentType);
}