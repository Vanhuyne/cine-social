package com.huy.backend.repository;

import com.huy.backend.models.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReviewRepo extends JpaRepository<Review, Long> {
    Page<Review> findByMovie_MovieId(Long movieId, Pageable pageable);

    Optional<Review> findByUser_UserIdAndMovie_MovieId(Long userId, Long movieId);

    // get 5 reviews of 5 movies recent
    @Query(value = "SELECT r.* FROM reviews r " +
            "INNER JOIN (SELECT movie_id, MAX(created_at) as max_created_at " +
            "           FROM reviews " +
            "           GROUP BY movie_id " +
            "           ORDER BY max_created_at DESC " +
            "           LIMIT 5) latest " +
            "ON r.movie_id = latest.movie_id " +
            "AND r.created_at = latest.max_created_at " +
            "ORDER BY r.created_at DESC",
            nativeQuery = true)
    List<Review> findTop5MostRecentReviewsByMovie();
}
