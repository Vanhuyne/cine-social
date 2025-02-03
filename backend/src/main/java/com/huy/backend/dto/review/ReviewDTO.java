package com.huy.backend.dto.review;

import com.huy.backend.models.Review;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class ReviewDTO {
    private Long userId;
    private Long movieId;
    private int rating;
    private String comment;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static ReviewDTO convertToDTO(Review review) {
        return ReviewDTO.builder()
                .userId(review.getUser().getUserId())
                .movieId(review.getMovie().getMovieId())
                .rating(review.getRating())
                .comment(review.getComment())
                .createdAt(review.getCreatedAt())
                .updatedAt(review.getUpdatedAt())
                .build();
    }

    public static Review convertToEntity(ReviewDTO reviewDTO) {
        return Review.builder()
                .rating(reviewDTO.getRating())
                .comment(reviewDTO.getComment())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }
}
