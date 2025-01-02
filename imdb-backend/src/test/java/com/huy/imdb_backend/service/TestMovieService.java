package com.huy.imdb_backend.service;

import com.huy.imdb_backend.dto.GenreDTO;
import com.huy.imdb_backend.dto.MovieDTO;
import com.huy.imdb_backend.models.Genre;
import com.huy.imdb_backend.models.Movie;
import com.huy.imdb_backend.repository.MovieRepo;
import com.huy.imdb_backend.service.impl.MovieServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;
//
//@AutoConfigureMockMvc
@SpringBootTest
@Slf4j
public class TestMovieService {

    @MockBean
    private MovieRepo movieRepo;

    @Autowired
    private MovieServiceImpl movieServiceImpl;

    private Movie movie1;
    private Movie movie2;
    private Pageable pageable ;

    @BeforeEach
    void setUp() {
//        MockitoAnnotations.openMocks(this);
        // Mock Data
        Genre thriller = new Genre(53L, "Thriller");
        Genre action = new Genre(28L, "Action");
        Genre adventure = new Genre(12L, "Adventure");

        Set<Genre> genres = new HashSet<>();
        genres.add(thriller);
        genres.add(action);
        genres.add(adventure);

        movie1 = new Movie(
                6279L,
                "Kraven the Hunter",
                LocalDate.parse("2024-12-11"),
                null,
                "Kraven Kravinoff's complex relationship with his ruthless gangster father, Nikolai, starts him down a path of vengeance with brutal consequences, motivating him to become not only the greatest hunter in the world, but also one of its most feared.",
                "/1GvBhRxY6MELDfxFrete6BNhBB5.jpg",
                "/v9Du2HC3hlknAvGlWhquRbeifwW.jpg",
                539972,
                LocalDateTime.parse("2024-12-25T10:22:53"),
                genres,
                1133.345,
                5.8,
                196
        );
        movie2 = new Movie(
                6279L,
                "Kraven the Hunter",
                LocalDate.parse("2024-12-11"),
                null,
                "Kraven Kravinoff's complex relationship with his ruthless gangster father, Nikolai, starts him down a path of vengeance with brutal consequences, motivating him to become not only the greatest hunter in the world, but also one of its most feared.",
                "/1GvBhRxY6MELDfxFrete6BNhBB5.jpg",
                "/v9Du2HC3hlknAvGlWhquRbeifwW.jpg",
                539972,
                LocalDateTime.parse("2024-12-25T10:22:53"),
                genres,
                1133.345,
                5.8,
                196
        );
        pageable = PageRequest.of(0, 2);
    }

    @Test
    public void testGetAllMovies() {
        // Given
        List<Movie> movies = List.of(movie1, movie2);
        Page<Movie> moviePage = new PageImpl<>(movies, pageable, movies.size());

        // Mock the repository call
        when(movieRepo.findAll(pageable)).thenReturn(moviePage);

        // When
        Page<MovieDTO> result = movieServiceImpl.getAllMovies(pageable);

        // Then
        assertNotNull(result.getContent());
        assertEquals(2, result.getContent().size());

        // Verify repository interaction
//        verify(movieRepo, times(1)).findAll(pageable);
    }

//    @Test
//    public void getAllMoviesReturnEmptyPage() {
//        Pageable pageable = PageRequest.of(0, 10);
//        Page<Movie> moviePage = new PageImpl<>(Collections.emptyList());
//
//        when(movieRepo.findAll(pageable)).thenReturn(moviePage);
//
//        Page<MovieDTO> result = movieService.getAllMovies(pageable);
//
//        assertEquals(0, result.getTotalElements());
//    }
}
