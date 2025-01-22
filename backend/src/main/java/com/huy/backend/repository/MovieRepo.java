package com.huy.backend.repository;

import com.huy.backend.models.Movie;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MovieRepo extends JpaRepository<Movie , Long> {

    @Query("SELECT m FROM Movie m WHERE " +
            "LOWER(m.title) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
            "CAST(m.overview as string) LIKE LOWER(CONCAT('%', :query, '%'))")
    Page<Movie> searchMovies(@Param("query") String query, Pageable pageable);

    Optional<Movie> findByTmdbId(Integer tmdbId);

    Page<Movie> findAll(Pageable pageable);

    List<Movie> findByRuntimeIsNull();
    List<Movie> findByTrailerKeyIsNull();

    Page<Movie> findAllByOrderByPopularityDesc(Pageable pageable);

}
