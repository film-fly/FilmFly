package com.sparta.filmfly.domain.media.repository;

import com.sparta.filmfly.domain.media.entity.Media;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MediaRepository  extends JpaRepository<Media, Long> {

}