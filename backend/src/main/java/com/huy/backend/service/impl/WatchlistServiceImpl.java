package com.huy.backend.service.impl;

import com.huy.backend.dto.MovieDTO;
import com.huy.backend.dto.watchlist.WatchlistDTO;
import com.huy.backend.exception.BadRequestException;
import com.huy.backend.exception.ResourceNotFoundException;
import com.huy.backend.models.Movie;
import com.huy.backend.models.User;
import com.huy.backend.models.Watchlist;
import com.huy.backend.repository.MovieRepo;
import com.huy.backend.repository.UserRepo;
import com.huy.backend.repository.WatchListRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WatchlistServiceImpl {
    private final WatchListRepo watchlistRepository;
    private final UserRepo userRepository;
    private final MovieRepo movieRepository;

    public void addMovieToWatchlist(Long watchListId , Long userId, Long movieId) {
        Watchlist watchlist = watchlistRepository.findById(watchListId)
                .orElseThrow(() -> new ResourceNotFoundException("Watchlist not found"));

        if(!watchlist.getUser().getUserId().equals(userId)) {
            throw new BadRequestException("User does not have permission to add movie to this watchlist");
        }

        Movie movie = movieRepository.findById(movieId)
                .orElseThrow(() -> new ResourceNotFoundException("Movie not found"));

        if (watchlist.getMovies().contains(movie)) {
            throw new BadRequestException("Movie already exists in watchlist");
        }

        watchlist.getMovies().add(movie);
        watchlistRepository.save(watchlist);
    }

    public WatchlistDTO createWatchlist(Long userId, String name) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));


        Watchlist watchlist = Watchlist.builder()
                .name(name)
                .user(user)
                .movies(new ArrayList<>())
                .createdAt(LocalDateTime.now())
                .build();

        return mapToDTO(watchlistRepository.save(watchlist));

    }

    private WatchlistDTO mapToDTO(Watchlist watchlist) {
        return WatchlistDTO.builder()
                .watchlistId(watchlist.getWatchlistId())
                .name(watchlist.getName())
                .userId(watchlist.getUser().getUserId())
                .movies(watchlist.getMovies().stream()
                        .map(this::mapMoviesToDTO)
                        .collect(Collectors.toList()))
                .createdAt(watchlist.getCreatedAt())
                .build();
    }
    private MovieDTO mapMoviesToDTO(Movie movie) {
        return MovieDTO.builder()
                .movieId(movie.getMovieId())
                .title(movie.getTitle())
                .posterPath(movie.getPosterPath())
                .releaseDate(movie.getReleaseDate())
                .runtime(movie.getRuntime())
                .build();
    }
}
