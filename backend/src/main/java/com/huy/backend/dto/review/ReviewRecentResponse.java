package com.huy.backend.dto.review;

import com.huy.backend.models.Review;
import com.huy.backend.models.Vote;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ReviewRecentResponse {
    private Long reviewId;
    private String userDisplayName;
    private String userAvatar;
    private Double rating;
    private String comment;
    private LocalDateTime createdAt;
    private ReviewResponse.MovieInfo movie;
    private Long totalVotes;

    public ReviewRecentResponse(Review review, Long upVotes, Long downVotes, ReviewResponse.MovieInfo movie) {
        this.reviewId = review.getReviewId();
        this.userDisplayName = review.getUser().getUsername();
        this.userAvatar = review.getUser().getProfilePicture();
        this.rating = review.getRating();
        this.comment = review.getComment();
        this.createdAt = review.getCreatedAt();
        this.movie = movie;
        this.totalVotes = upVotes + downVotes;
    }
}
