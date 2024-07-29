package com.sparta.filmfly.domain.media.repository;

import com.sparta.filmfly.domain.media.entity.Media;
import com.sparta.filmfly.domain.media.entity.MediaTypeEnum;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MediaRepository  extends JpaRepository<Media, Long> {
    List<Media> findAllByTypeAndTypeId(MediaTypeEnum type, Long typeId);
}