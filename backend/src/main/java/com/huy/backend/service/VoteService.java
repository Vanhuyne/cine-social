package com.huy.backend.service;

import com.huy.backend.dto.vote.VoteRequest;
import com.huy.backend.exception.ResourceNotFoundException;
import com.huy.backend.models.Review;
import com.huy.backend.models.User;
import com.huy.backend.models.Vote;
import com.huy.backend.repository.ReviewRepo;
import com.huy.backend.repository.VoteRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class VoteService {
    private final ReviewRepo reviewRepo;
    private final UserService userService;
    private final VoteRepo voteRepo;

    public void upVote(VoteRequest voteRequest) {
        User user = userService.getAuthCurrent();
        Review review = reviewRepo.findById(voteRequest.getReviewId())
                .orElseThrow(() -> new ResourceNotFoundException("Review not found with id " + voteRequest.getReviewId()));

        Optional<Vote> optionalVote = voteRepo.findByUserAndReview(user, review);
        if (optionalVote.isPresent()) {
            Vote existingVote = optionalVote.get();
            if (existingVote.getVoteType().equals(voteRequest.getVoteType())) {
                // remove vote
                voteRepo.delete(existingVote);
            } else {
                existingVote.setVoteType(voteRequest.getVoteType());
                voteRepo.save(existingVote);
            }
        } else {
            // create vote
            Vote vote = new Vote();
            vote.setUser(user);
            vote.setReview(review);
            vote.setVoteType(voteRequest.getVoteType());
            voteRepo.save(vote);
        }
    }
}
