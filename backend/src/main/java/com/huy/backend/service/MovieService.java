package com.huy.backend.service;

import com.huy.backend.dto.GenreDTO;
import com.huy.backend.dto.Movie.MovieCreateDTO;
import com.huy.backend.dto.Movie.MovieDTO;
import com.huy.backend.dto.Movie.MovieUpdateDTO;
import com.huy.backend.exception.ResourceNotFoundException;
import com.huy.backend.models.Genre;
import com.huy.backend.models.Movie;
import com.huy.backend.repository.MovieRepo;
import com.huy.backend.utils.GenerateRandom;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class MovieService {

    private final MovieRepo movieRepo;
    private final CacheManager cacheManager;

    @Cacheable(value = "moviesCache", key = "'allMovies-' + #pageable.pageNumber + '-' + #pageable.pageSize")
    public Page<MovieDTO> getAllMovies(Pageable pageable) {
        return movieRepo.findAll(pageable).map(this::convertToMovieDTO);
    }

    public MovieDTO getMovieById(Long movieId) {
        return movieRepo.findById(movieId)
                .map(this::convertToMovieDTO)
                .orElseThrow(() -> new ResourceNotFoundException("Movie not found"));
    }

    @CacheEvict(value = "moviesCache", allEntries = true)
    public MovieDTO addMovie(MovieCreateDTO movieDTO) {
        if (movieRepo.findByTmdbId(movieDTO.getTmdbId()).isPresent()) {
            throw new ResourceNotFoundException("Movie already exists");
        }

        return convertToMovieDTO(movieRepo.save(convertCreatToMovie(movieDTO)));
    }

    @CacheEvict(value = "moviesCache", allEntries = true)
    public MovieDTO updateMovie(Long movieId, MovieUpdateDTO movieDTO) {
        movieRepo.findById(movieId)
                .orElseThrow(() -> new ResourceNotFoundException("Movie not found"));

        Movie updateMovie = convertUpdateToMovie(movieDTO);
        updateMovie.setMovieId(movieId);

        return convertToMovieDTO(movieRepo.save(updateMovie));
    }

    @CacheEvict(value = "moviesCache", allEntries = true)
    public void deleteMovie(Long movieId) {
        movieRepo.deleteById(movieId);
    }

    @Cacheable(value = "moviesCache", key = "#query + '-' + #page + '-' + #size")
    public Page<MovieDTO> searchMovies(String query, int page, int size) {
        log.info("Caching search results for query: {}, page: {}, size: {}", query, page, size);
        Pageable pageable = PageRequest.of(page, size, Sort.by("popularity").descending());
        return movieRepo.searchMovies(query, pageable).map(this::convertToMovieDTO);
    }


    public MovieDTO convertToMovieDTO(Movie movie) {
        return MovieDTO.builder()
                .movieId(movie.getMovieId())
                .title(movie.getTitle())
                .releaseDate(movie.getReleaseDate())
                .runtime(movie.getRuntime())
                .overview(movie.getOverview())
                .posterPath(movie.getPosterPath())
                .backdropPath(movie.getBackdropPath())
                .tmdbId(movie.getTmdbId())
                .createdAt(movie.getCreatedAt())
                .popularity(movie.getPopularity())
                .voteAverage(movie.getVoteAverage())
                .voteCount(movie.getVoteCount())
                .voteCount(movie.getVoteCount())
                .trailerKey(movie.getTrailerKey())
                .genres(movie.getGenres().stream().map(
                        this::convertToGenreDTO)
                        .collect(Collectors.toSet()))
                .build();
    }
    private Movie convertToMovie(MovieDTO movieDTO) {
        return Movie.builder()
                .movieId(movieDTO.getMovieId())
                .title(movieDTO.getTitle())
                .releaseDate(movieDTO.getReleaseDate())
                .runtime(movieDTO.getRuntime())
                .overview(movieDTO.getOverview())
                .posterPath(movieDTO.getPosterPath())
                .backdropPath(movieDTO.getBackdropPath())
                .tmdbId(movieDTO.getTmdbId())
                .createdAt(movieDTO.getCreatedAt())
                .popularity(movieDTO.getPopularity())
                .voteAverage(movieDTO.getVoteAverage())
                .voteCount(movieDTO.getVoteCount())
                .trailerKey(movieDTO.getTrailerKey())
                .genres(movieDTO.getGenres().stream().map(
                        this::convertToGenre)
                        .collect(Collectors.toSet()))
                .build();
    }

    private GenreDTO convertToGenreDTO(Genre genre) {
        return GenreDTO.builder()
                .genreId(genre.getGenreId())
                .name(genre.getName())
                .build();
    }
    private Genre convertToGenre(GenreDTO genreDTO) {
        return Genre.builder()
                .genreId(genreDTO.getGenreId())
                .name(genreDTO.getName())
                .build();
    }

    private Movie convertUpdateToMovie(MovieUpdateDTO movieDTO) {
        return Movie.builder()
                .title(movieDTO.getTitle())
                .releaseDate(movieDTO.getReleaseDate())
                .runtime(movieDTO.getRuntime())
                .overview(movieDTO.getOverview())
                .posterPath(movieDTO.getPosterPath())
                .backdropPath(movieDTO.getBackdropPath())
                .tmdbId(movieDTO.getTmdbId())
                .createdAt(movieDTO.getCreatedAt())
                .popularity(movieDTO.getPopularity())
                .voteAverage(movieDTO.getVoteAverage())
                .voteCount(movieDTO.getVoteCount())
                .trailerKey(movieDTO.getTrailerKey())
                .genres(movieDTO.getGenreIds().stream().map(
                        genreId -> Genre.builder().genreId(genreId).build())
                        .collect(Collectors.toSet()))
                .build();
    }

    private Movie convertCreatToMovie(MovieCreateDTO movieDTO) {
        return Movie.builder()
                .movieId(GenerateRandom.generateRandomLong())
                .title(movieDTO.getTitle())
                .releaseDate(movieDTO.getReleaseDate())
                .runtime(movieDTO.getRuntime())
                .overview(movieDTO.getOverview())
                .posterPath(movieDTO.getPosterPath())
                .backdropPath(movieDTO.getBackdropPath())
                .tmdbId(movieDTO.getTmdbId())
                .createdAt(movieDTO.getCreatedAt())
                .popularity(movieDTO.getPopularity())
                .voteAverage(movieDTO.getVoteAverage())
                .voteCount(movieDTO.getVoteCount())
                .trailerKey(movieDTO.getTrailerKey())
                .genres(movieDTO.getGenreIds().stream().map(
                                genreId -> Genre.builder().genreId(genreId).build())
                        .collect(Collectors.toSet()))
                .build();
    }

}

