package com.huy.backend.controller;

import com.huy.backend.dto.watchlist.WatchlistDTO;
import com.huy.backend.dto.watchlist.WatchlistDetailDTO;
import com.huy.backend.service.impl.WatchlistServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/watchlist")
@RequiredArgsConstructor
public class WatchListController {
    private final WatchlistServiceImpl watchListService;

    @GetMapping("/user")
    public ResponseEntity<List<WatchlistDTO>> getWatchListByUserId(){
        List<WatchlistDTO> listWatchListlDto = watchListService.getWatchlistsByUserId();
        return ResponseEntity.ok(listWatchListlDto);
    }
    
    @GetMapping("/{watchListId}")
    public ResponseEntity<WatchlistDetailDTO> getWatchListDetail(@PathVariable Long watchListId){
        WatchlistDetailDTO watchlistDetailDTO = watchListService.getWatchlistById(watchListId);
        return ResponseEntity.ok(watchlistDetailDTO);
    }

    @PostMapping("/{watchListId}/movie/{movieId}")
    public ResponseEntity<Void> addWatchList(@PathVariable Long watchListId , @RequestParam Long userId , @PathVariable Long movieId){
        watchListService.addMovieToWatchlist(watchListId, userId, movieId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/create")
    public ResponseEntity<WatchlistDetailDTO> createWatchList(@RequestParam Long userId , @RequestParam String name){
        WatchlistDetailDTO watchlistDetailDTO = watchListService.createWatchlist(userId, name);
        return new ResponseEntity<>(watchlistDetailDTO, HttpStatus.CREATED);
    }
}
