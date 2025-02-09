package com.huy.backend.service;

import com.huy.backend.dto.review.ReviewRequest;
import com.huy.backend.dto.review.ReviewResponse;
import com.huy.backend.exception.ResourceNotFoundException;
import com.huy.backend.models.Movie;
import com.huy.backend.models.Review;
import com.huy.backend.models.User;
import com.huy.backend.models.Vote;
import com.huy.backend.repository.MovieRepo;
import com.huy.backend.repository.ReviewRepo;
import com.huy.backend.repository.VoteRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ReviewService {
    private final ReviewRepo reviewRepo;
    private final UserService userService;
    private final MovieRepo movieRepo;
    private final VoteRepo voteRepo;

    public Page<ReviewResponse> getReviewsByMovieId(Long movieId, int page, int size) {
        Pageable pageable = Pageable.ofSize(size).withPage(page);
        Page<Review> reviewPage = reviewRepo.findByMovie_MovieId(movieId, pageable);

        User user = getAuthenticatedUserOrNull();

        return reviewPage.map(review -> new ReviewResponse(
                review,
                voteRepo.countByReviewAndVoteType(review, Vote.VoteType.UP),
                voteRepo.countByReviewAndVoteType(review, Vote.VoteType.DOWN),
                (user != null) ? voteRepo.findByUserAndReview(user, review).map(Vote::getVoteType).orElse(null) : null
        ));
    }

    // add review
    public ReviewResponse createReview(ReviewRequest reviewRequest) {
        User user = userService.getAuthCurrent();
        Movie movie = movieRepo.findById(reviewRequest.getMovieId())
                .orElseThrow(() -> new ResourceNotFoundException("Movie not found"));

        Review review = new Review();
        review.setUser(user);
        review.setMovie(movie);
        review.setRating(reviewRequest.getRating());
        review.setComment(reviewRequest.getComment());
        review.setCreatedAt(LocalDateTime.now());
        review.setUpdatedAt(LocalDateTime.now());

        return ReviewResponse.convertToReviewResponse(reviewRepo.save(review));
    }

    // update review
    public ReviewResponse updateReview(Long reviewId, ReviewRequest reviewRequest) {
        Review review = reviewRepo.findById(reviewId).orElseThrow(
                () -> new RuntimeException("Review not found")
        );
        review.setRating(reviewRequest.getRating());
        review.setComment(reviewRequest.getComment());
        review.setUpdatedAt(LocalDateTime.now());

        return ReviewResponse.convertToReviewResponse(reviewRepo.save(review));

    }

    // delete review
    public void deleteReview(Long reviewId) {
        Review review = reviewRepo.findById(reviewId).orElseThrow(
                () -> new RuntimeException("Review not found")
        );
        reviewRepo.delete(review);
    }

    private User getAuthenticatedUserOrNull() {
        try {
            return userService.getAuthCurrent();
        } catch (Exception e) {
            return null;
        }
    }


}
