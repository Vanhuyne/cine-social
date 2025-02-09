package com.huy.backend.controller;

import com.huy.backend.dto.vote.VoteRequest;
import com.huy.backend.service.VoteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/vote")

public class VoteController {
    private final VoteService voteService;

    @PostMapping("/upvote")
    public ResponseEntity<Void> upVote(@RequestBody VoteRequest voteRequest) {
        voteService.upVote(voteRequest);
        return ResponseEntity.ok().build();
    }
}
