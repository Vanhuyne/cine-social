package com.huy.backend.controller;

import com.huy.backend.dto.review.ReviewRequest;
import com.huy.backend.dto.review.ReviewResponse;
import com.huy.backend.service.ReviewService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    @GetMapping("/movie/{movieId}")
    public ResponseEntity<List<ReviewResponse>> getReviewsByMovieId(@PathVariable Long movieId) {
        return new ResponseEntity<>(reviewService.getReviewsByMovieId(movieId), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<ReviewResponse> addReview(
            @RequestBody @Valid ReviewRequest reviewRequest) {
        ReviewResponse reviewResponse = reviewService.createReview(reviewRequest);
        return new ResponseEntity<>(reviewResponse, HttpStatus.CREATED);
    }

    @DeleteMapping("/{reviewId}")
    public ResponseEntity<Void> deleteReview(@PathVariable Long reviewId) {
        reviewService.deleteReview(reviewId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
