package com.huy.backend.dto.vote;

import com.huy.backend.models.Vote;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VoteRequest {
    private Long reviewId;
    private Vote.VoteType voteType;
}
