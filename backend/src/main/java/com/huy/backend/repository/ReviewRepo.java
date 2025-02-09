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

//    @Query("""
//                SELECT new com.huy.backend.dto.vote.ReviewVoteCount(
//                    r.id,
//                    COUNT(CASE WHEN v.voteType = 'UP' THEN 1 END),
//                    COUNT(CASE WHEN v.voteType = 'DOWN' THEN 1 END)
//                )
//                FROM Review r
//                LEFT JOIN Vote v ON r.id = v.review.id
//                GROUP BY r.id
//            """)
//    List<ReviewVoteCount> findReviewVoteCounts();
}
