package com.huy.backend.controller;

import com.huy.backend.dto.review.ReviewDTO;
import com.huy.backend.models.Review;
import com.huy.backend.models.User;
import com.huy.backend.service.ReviewService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    @GetMapping("/{movieId}")
    public ResponseEntity<List<ReviewDTO>> getReviewsByMovieId(@PathVariable Long movieId) {
        return new ResponseEntity<>(reviewService.getReviewsByMovieId(movieId), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<ReviewDTO> addReview(
            @RequestBody @Valid ReviewDTO reviewDTO,
            @AuthenticationPrincipal User user) {
        ReviewDTO reviewResponse = reviewService.createReview(user.getUserId(), reviewDTO);
        return new ResponseEntity<>(reviewResponse, HttpStatus.CREATED);
    }
}
