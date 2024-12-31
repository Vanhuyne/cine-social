package com.huy.imdb_backend.service.impl;

import com.huy.imdb_backend.dto.GenreDTO;
import com.huy.imdb_backend.dto.MovieDTO;
import com.huy.imdb_backend.exception.ResourceNotFoundException;
import com.huy.imdb_backend.models.Genre;
import com.huy.imdb_backend.models.Movie;
import com.huy.imdb_backend.repository.MovieRepo;
import com.huy.imdb_backend.service.MovieService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class MovieServiceImpl implements MovieService {

    private final MovieRepo movieRepo;
    private final CacheManager cacheManager;

    @Override
    public Page<MovieDTO> getAllMovies(Pageable pageable) {
        return movieRepo.findAll(pageable).map(
                this::convertToMovieDTO
        );
    }

    // get movie by id
    @Override
    public MovieDTO getMovieById(Long movieId) {
        return movieRepo.findById(movieId)
                .map(this::convertToMovieDTO)
                .orElseThrow(() -> new ResourceNotFoundException("Movie not found"));

    }

    @Override
    public MovieDTO addMovie(MovieDTO movieDTO) {
        // check if movie already exists
        // if exists, return error
        Optional<Movie> existingMovie = movieRepo.findByTmdbId(movieDTO.getTmdbId());
        if (existingMovie.isPresent()) {
            throw new ResourceNotFoundException("Movie already exists");
        }
        Movie movie = convertToMovie(movieDTO);
        return convertToMovieDTO(movieRepo.save(movie));
    }

    @Override
    public MovieDTO updateMovie(Long movieId, MovieDTO movieDTO) {
        // check if movie exists so we can update it
        Movie existingMovie = movieRepo.findById(movieId)
                .orElseThrow(() -> new ResourceNotFoundException("Movie not found"));

        Movie movie = convertToMovie(movieDTO);
        return convertToMovieDTO(movieRepo.save(movie));
    }

    @Override
    public void deleteMovie(Long movieId) {

    }

    @Override
    @Cacheable(value = "moviesCache",
                key = "#query + '-' + #page + '-' + #size")
    public Page<MovieDTO> searchMovies(String query, int page, int size) {
        log.info("Caching search results for query: {}, page: {}, size: {}", query, page, size);
        Pageable pageable = PageRequest.of(page, size, Sort.by("popularity").descending());
        Page<Movie> movies = movieRepo.searchMovies(query, pageable);
        return movies.map(this::convertToMovieDTO);
    }

    public boolean isDataCached(String query, int page, int size) {
        String cacheKey = query + '-' + page + '-' + size;
        log.info("Checking if data is cached for key: {}", cacheKey);
        boolean isCached = cacheManager.getCache("moviesCache").get(cacheKey) != null;
        log.info("Data cached for key {}: {}", cacheKey, isCached);
        return isCached;
    }

    // clear cache when new movie is added , updated or deleted

    private MovieDTO convertToMovieDTO(Movie movie) {
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
}
