package com.huy.backend.service;

import com.huy.backend.dto.MovieDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface MovieService {
    Page<MovieDTO> getAllMovies(Pageable pageable);
    MovieDTO getMovieById(Long movieId);
    MovieDTO addMovie(MovieDTO movieDTO);
    MovieDTO updateMovie(Long movieId, MovieDTO movieDTO);
    void deleteMovie(Long movieId);
    Page<MovieDTO> searchMovies(String query, int page, int size);
}
