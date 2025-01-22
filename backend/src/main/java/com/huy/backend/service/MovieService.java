package com.huy.backend.service;

import com.huy.backend.dto.GenreDTO;
import com.huy.backend.dto.movie.MovieCreateDTO;
import com.huy.backend.dto.movie.MovieDTO;
import com.huy.backend.dto.movie.MovieUpdateDTO;
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

    @Cacheable(value = "moviesCache", key = "'allMoviesByPopularity-' + #pageable.pageNumber + '-' + #pageable.pageSize")
    public Page<MovieDTO> getAllMoviesByPopularity(Pageable pageable) {
        return movieRepo.findAllByOrderByPopularityDesc(pageable).map(this::convertToMovieDTO);
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

        Movie movie = MovieCreateDTO.convertToMovie(movieDTO);

        return convertToMovieDTO(movieRepo.save(movie));
    }

    @CacheEvict(value = "moviesCache", allEntries = true)
    public MovieDTO updateMovie(Long movieId, MovieUpdateDTO movieDTO) {
        Movie movieExist = movieRepo.findById(movieId)
                .orElseThrow(() -> new ResourceNotFoundException("Movie not found"));

       if (movieRepo.findByTmdbId(movieDTO.getTmdbId()).isPresent() && !movieExist.getTmdbId().equals(movieDTO.getTmdbId())) {
            throw new ResourceNotFoundException("Movie with TMDB ID " + movieDTO.getTmdbId() + " already exists");
       }

       updateMovieFromDTO(movieExist, movieDTO);

       Movie updatedMovie = movieRepo.save(movieExist);
       return convertToMovieDTO(updatedMovie);

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
                        GenreDTO::convertToGenreDTO)
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
                        GenreDTO::convertToGenre)
                        .collect(Collectors.toSet()))
                .build();
    }


    private void updateMovieFromDTO(Movie movie, MovieUpdateDTO dto) {
        movie.setTitle(dto.getTitle());
        movie.setReleaseDate(dto.getReleaseDate());
        movie.setRuntime(dto.getRuntime());
        movie.setOverview(dto.getOverview());
        movie.setPosterPath(dto.getPosterPath());
        movie.setBackdropPath(dto.getBackdropPath());
        movie.setTmdbId(dto.getTmdbId());
        movie.setGenres(dto.getGenreIds().stream().map(
                genreId -> Genre.builder().genreId(genreId).build())
                .collect(Collectors.toSet()));
        movie.setTrailerKey(dto.getTrailerKey());
    }

}

