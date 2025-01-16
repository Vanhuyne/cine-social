package com.huy.backend.controller;

import com.huy.backend.dto.watchlist.WatchlistDTO;
import com.huy.backend.service.impl.WatchlistServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/watchlist")
@RequiredArgsConstructor
public class WatchListController {
    private final WatchlistServiceImpl watchListService;

    @PostMapping("/{watchListId}/movie/{movieId}")
    public ResponseEntity<Void> addWatchList(@PathVariable Long watchListId , @RequestParam Long userId , @PathVariable Long movieId){
        watchListService.addMovieToWatchlist(watchListId, userId, movieId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/create")
    public ResponseEntity<WatchlistDTO> createWatchList(@RequestParam Long userId , @RequestParam String name){
        WatchlistDTO watchlistDTO = watchListService.createWatchlist(userId, name);
        return new ResponseEntity<>(watchlistDTO, HttpStatus.CREATED);
    }
}
