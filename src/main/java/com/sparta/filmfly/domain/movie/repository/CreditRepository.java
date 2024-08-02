package com.sparta.filmfly.domain.movie.repository;

import com.sparta.filmfly.domain.movie.entity.Credit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CreditRepository extends JpaRepository<Credit, Long>, CreditRepositoryCustom {
    @Query("SELECT c FROM Credit c WHERE c.name LIKE %:keyword%")
    List<Credit> findCreditsByName(@Param("keyword") String Name);

    @Query("SELECT c FROM Credit c WHERE c.originalName LIKE %:keyword%")
    List<Credit> findCreditsByOriginalName(@Param("keyword") String OriginalName);
}
