package com.huy.backend.service.impl;

import com.huy.backend.dto.watchlist.MovieWatchListDTO;
import com.huy.backend.dto.watchlist.WatchlistDTO;
import com.huy.backend.dto.watchlist.WatchlistDetailDTO;
import com.huy.backend.exception.BadRequestException;
import com.huy.backend.exception.ResourceNotFoundException;
import com.huy.backend.models.Movie;
import com.huy.backend.models.User;
import com.huy.backend.models.Watchlist;
import com.huy.backend.repository.MovieRepo;
import com.huy.backend.repository.UserRepo;
import com.huy.backend.repository.WatchListRepo;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WatchlistServiceImpl {
    private final WatchListRepo watchlistRepository;
    private final UserRepo userRepository;
    private final MovieRepo movieRepository;
    private final AuthenticationManager authenticationManager;

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

    public WatchlistDetailDTO createWatchlist(String name) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        User existUser = userRepository.findById(user.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));


        Watchlist watchlist = Watchlist.builder()
                .name(name)
                .user(existUser)
                .movies(new ArrayList<>())
                .createdAt(LocalDateTime.now())
                .build();

        return mapToWatchlistDetailDTO(watchlistRepository.save(watchlist));

    }

    public WatchlistDetailDTO getWatchlistById(Long watchlistId) {
        Watchlist watchlist = watchlistRepository.findById(watchlistId)
                .orElseThrow(() -> new ResourceNotFoundException("Watchlist not found"));
        return mapToWatchlistDetailDTO(watchlist);
    }

    // get all watchlists by user id
    public List<WatchlistDTO> getWatchlistsByUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        return watchlistRepository.findByUser_UserId(user.getUserId()).stream()
                .map(this::mapToWatchlistDTO)
                .collect(Collectors.toList());
    }

    // remove movie from watchlist
    public void removeMovieFromWatchlist(Long watchlistId, Long movieId) {
        Watchlist watchlist = watchlistRepository.findById(watchlistId)
                .orElseThrow(() -> new ResourceNotFoundException("Watchlist not found"));

        Movie movie = movieRepository.findById(movieId).orElseThrow(() -> new ResourceNotFoundException("Movie not found"));

        if (!watchlist.getMovies().contains(movie)) {
            throw new BadRequestException("Movie does not exist in watchlist");
        }

        watchlist.getMovies().remove(movie);
        watchlistRepository.save(watchlist);
    }

    @Transactional
    public void deleteWatchlist(Long watchlistId) {
        Watchlist watchlist = watchlistRepository.findById(watchlistId)
                .orElseThrow(() -> new ResourceNotFoundException("Watchlist not found"));
        watchlistRepository.delete(watchlist);
    }

    private WatchlistDTO mapToWatchlistDTO(Watchlist watchlist) {
        return WatchlistDTO.builder()
                .watchlistId(watchlist.getWatchlistId())
                .name(watchlist.getName())
                .build();
    }

    private WatchlistDetailDTO mapToWatchlistDetailDTO(Watchlist watchlist) {
        return WatchlistDetailDTO.builder()
                .watchlistId(watchlist.getWatchlistId())
                .name(watchlist.getName())
                .userId(watchlist.getUser().getUserId())
                .movies(watchlist.getMovies().stream()
                        .map(this::mapMoviesWatchlistToDTO)
                        .collect(Collectors.toList()))
                .createdAt(watchlist.getCreatedAt())
                .build();
    }
    private MovieWatchListDTO mapMoviesWatchlistToDTO(Movie movie) {
        return MovieWatchListDTO.builder()
                .movieId(movie.getMovieId())
                .title(movie.getTitle())
                .posterPath(movie.getPosterPath())
                .releaseDate(movie.getReleaseDate())
                .runtime(movie.getRuntime())
                .build();
    }
}
