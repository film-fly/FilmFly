package com.sparta.filmfly.domain.collection.repository;

import com.sparta.filmfly.domain.collection.entity.Collection;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CollectionRepository extends JpaRepository<Collection, Long> {

}