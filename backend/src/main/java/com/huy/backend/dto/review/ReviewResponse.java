package com.huy.backend.dto.review;

import com.huy.backend.models.Review;
import com.huy.backend.models.Vote;
import lombok.*;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class ReviewResponse {
    private Long reviewId;
    private String userDisplayName;
    private Double rating;
    private String comment;
    private Long upVotes;
    private Long downVotes;
    private Vote.VoteType userVote;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private MovieInfo movie;
    private Long totalVotes;

    public static ReviewResponse convertToReviewResponse(Review review) {
        return ReviewResponse.builder()
                .reviewId(review.getReviewId())
                .userDisplayName(review.getUser().getUsername())
                .rating(review.getRating())
                .comment(review.getComment())
                .createdAt(review.getCreatedAt())
                .updatedAt(review.getUpdatedAt())
                .build();
    }

    // new constructor
    public ReviewResponse(Review review, Long upVotes, Long downVotes, Vote.VoteType userVote) {
        this.reviewId = review.getReviewId();
        this.userDisplayName = review.getUser().getUsername();
        this.rating = review.getRating();
        this.comment = review.getComment();
        this.createdAt = review.getCreatedAt();
        this.updatedAt = review.getUpdatedAt();
        this.upVotes = upVotes;
        this.downVotes = downVotes;
        this.userVote = userVote;
    }


    @Getter
    @Setter
    @AllArgsConstructor
    public static class MovieInfo {
        private Long movieId;
        private String title;
    }
}
