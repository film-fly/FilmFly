package com.sparta.filmfly.domain.officeboard.repository;

import com.sparta.filmfly.domain.officeboard.entity.OfficeBoard;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OfficeBoardRepository extends JpaRepository<OfficeBoard, Long> {

}