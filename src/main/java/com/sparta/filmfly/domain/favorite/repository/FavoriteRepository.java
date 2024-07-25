package com.sparta.filmfly.domain.favorite.repository;

import com.sparta.filmfly.domain.favorite.entity.Favorite;
import java.util.List;
import java.util.Objects;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface FavoriteRepository extends JpaRepository<Favorite, Long> {

    boolean existsFavoriteByMovieIdAndUserId(Long movieId, Long userId);

//    @Query("SELECT * FROM favorite f WHERE f.deleted_at IS NOT NULL", nativeQuery = true)
//    List<Favorite> findAllByDeletedAtIsNotNull();
//
//    boolean deleteById(long id);
}