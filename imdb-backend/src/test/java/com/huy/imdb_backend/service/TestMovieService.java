package com.huy.imdb_backend.service;

import com.huy.imdb_backend.dto.MovieDTO;
import com.huy.imdb_backend.models.Movie;
import com.huy.imdb_backend.repository.MovieRepo;
import com.huy.imdb_backend.service.impl.MovieServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;


public class TestMovieService {
    @Mock
    private MovieRepo movieRepo;

    @InjectMocks
    private MovieServiceImpl movieService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetAllMovies() {
        Pageable pageable = PageRequest.of(0, 10);
        Movie movie = new Movie();
        movie.setMovieId(1L);
        movie.setTitle("Test Movie");
        Page<Movie> moviePage = new PageImpl<>(Collections.singletonList(movie));

        when(movieRepo.findAll(pageable)).thenReturn(moviePage);

        Page<MovieDTO> result = movieService.getAllMovies(pageable);

        // expected result not null
        assertEquals(1, result.getTotalElements());

//        assertEquals(1, result.getTotalElements());
//        assertEquals("Test Movie", result.getContent().get(0).getTitle());
    }
}
