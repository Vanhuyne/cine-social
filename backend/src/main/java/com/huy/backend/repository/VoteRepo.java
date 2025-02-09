package com.huy.backend.repository;

import com.huy.backend.models.Review;
import com.huy.backend.models.User;
import com.huy.backend.models.Vote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VoteRepo extends JpaRepository<Vote, Long> {
    Optional<Vote> findByUserAndReview(User user, Review review);

//    Optional<Vote> findByReviewAndUser(Review review, User user);

    Long countByReviewAndVoteType(Review review, Vote.VoteType voteType);
}
