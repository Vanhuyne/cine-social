package com.huy.backend.service;

import com.huy.backend.dto.review.ReviewDTO;
import com.huy.backend.exception.ResourceNotFoundException;
import com.huy.backend.models.Movie;
import com.huy.backend.models.Review;
import com.huy.backend.models.User;
import com.huy.backend.repository.MovieRepo;
import com.huy.backend.repository.ReviewRepo;
import com.huy.backend.repository.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ReviewService {
    private final ReviewRepo reviewRepo;
    private final UserRepo userRepo;
    private final MovieRepo movieRepo;

    public List<ReviewDTO> getReviewsByMovieId(Long movieId) {
        List<ReviewDTO> reviewDTOs = reviewRepo.findByMovie_MovieId(movieId).stream()
                .map(ReviewDTO::convertToDTO)
                .toList();
        return reviewDTOs;
    }

    // add review
    public ReviewDTO createReview(Long userId, ReviewDTO reviewDTO) {

        User user = userRepo.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Movie movie = movieRepo.findById(reviewDTO.getMovieId())
                .orElseThrow(() -> new ResourceNotFoundException("Movie not found"));

        Review review = new Review();
        review.setUser(user);
        review.setMovie(movie);
        review.setRating(reviewDTO.getRating());
        review.setComment(reviewDTO.getComment());
        review.setCreatedAt(LocalDateTime.now());
        review.setUpdatedAt(LocalDateTime.now());

        return ReviewDTO.convertToDTO(reviewRepo.save(review));
    }


    // update review
    public ReviewDTO updateReview(Long reviewId, ReviewDTO reviewDTO) {
        Review review = reviewRepo.findById(reviewId).orElseThrow(
                () -> new RuntimeException("Review not found")
        );
        review.setRating(reviewDTO.getRating());
        review.setComment(reviewDTO.getComment());
        review.setUpdatedAt(LocalDateTime.now());

        return ReviewDTO.convertToDTO(reviewRepo.save(review));

    }
}
