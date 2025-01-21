package com.huy.backend.controller;

import com.huy.backend.dto.Movie.MovieCreateDTO;
import com.huy.backend.dto.Movie.MovieDTO;
import com.huy.backend.dto.Movie.MovieUpdateDTO;
import com.huy.backend.service.MovieService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/movies")
@RequiredArgsConstructor
public class MovieController {
    private final MovieService movieService;

    @GetMapping("/all")
    public ResponseEntity<Page<MovieDTO>>getAllMovies(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = Pageable.ofSize(size).withPage(page);
        return new ResponseEntity<>(movieService.getAllMovies(pageable), HttpStatus.OK);
    }

    @GetMapping("/search")
    public ResponseEntity<Page<MovieDTO>> searchMovies(
            @RequestParam String query,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Page<MovieDTO> movieDTOS =  movieService.searchMovies(query, page, size);
        return new ResponseEntity<>(movieDTOS, HttpStatus.OK);
    }

    @GetMapping("/{movieId}")
    public ResponseEntity<MovieDTO> getMovieById(@PathVariable Long movieId) {
        return new ResponseEntity<>(movieService.getMovieById(movieId), HttpStatus.OK);
    }

    @PostMapping("/add")
    public ResponseEntity<MovieDTO> addMovie(@RequestBody @Valid MovieCreateDTO movieDTO) {
        return new ResponseEntity<>(movieService.addMovie(movieDTO), HttpStatus.CREATED);
    }

    @PutMapping("/{movieId}")
    public ResponseEntity<MovieDTO> updateMovie(@PathVariable Long movieId, @RequestBody @Valid MovieUpdateDTO movieDTO) {
        return new ResponseEntity<>(movieService.updateMovie(movieId, movieDTO), HttpStatus.OK);
    }

    @DeleteMapping("/{movieId}")
    public ResponseEntity<Void> deleteMovie(@PathVariable Long movieId) {
        movieService.deleteMovie(movieId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }


}
