package com.huy.backend.service;

import com.huy.backend.dto.review.ReviewRequest;
import com.huy.backend.dto.review.ReviewResponse;
import com.huy.backend.exception.ResourceNotFoundException;
import com.huy.backend.models.Movie;
import com.huy.backend.models.Review;
import com.huy.backend.models.User;
import com.huy.backend.repository.MovieRepo;
import com.huy.backend.repository.ReviewRepo;
import com.huy.backend.repository.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewService {
    private final ReviewRepo reviewRepo;
    private final UserRepo userRepo;
    private final MovieRepo movieRepo;

    public Page<ReviewResponse> getReviewsByMovieId(Long movieId, int page, int size) {
        Pageable pageable = Pageable.ofSize(size).withPage(page);
        Page<ReviewResponse> reviewDTOs = reviewRepo.findByMovie_MovieId(movieId, pageable)
                .map(ReviewResponse::convertToReviewResponse);
        return reviewDTOs;
    }

    // add review
    public ReviewResponse createReview(ReviewRequest reviewRequest) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();

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
}
