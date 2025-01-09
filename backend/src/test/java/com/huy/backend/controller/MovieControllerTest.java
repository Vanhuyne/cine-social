package com.huy.backend.controller;

import com.huy.backend.dto.GenreDTO;
import com.huy.backend.dto.MovieDTO;
import com.huy.backend.exception.ResourceNotFoundException;
import com.huy.backend.service.impl.MovieServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@AutoConfigureMockMvc
@SpringBootTest
public class MovieControllerTest {

    // init mock mvc
     @Autowired
     private MockMvc mockMvc;

    // Mock the service
    @MockBean
    private MovieServiceImpl movieServiceImpl;

    private MovieDTO movie;

    @BeforeEach
    void initData(){
        // Mock Data
        GenreDTO thriller = new GenreDTO(53L, "Thriller");
        GenreDTO action = new GenreDTO(28L, "Action");
        GenreDTO adventure = new GenreDTO(12L, "Adventure");

        Set<GenreDTO> genres = new HashSet<>();
        genres.add(thriller);
        genres.add(action);
        genres.add(adventure);

        movie = new MovieDTO(
                6279L,
                "Kraven the Hunter",
                LocalDate.parse("2024-12-11"),
                250,
                "Kraven Kravinoff's complex relationship with his ruthless gangster father, Nikolai, starts him down a path of vengeance with brutal consequences, motivating him to become not only the greatest hunter in the world, but also one of its most feared.",
                "/1GvBhRxY6MELDfxFrete6BNhBB5.jpg",
                "/v9Du2HC3hlknAvGlWhquRbeifwW.jpg",
                539972,
                LocalDateTime.parse("2024-12-25T10:22:53"),
                genres,
                1133.345,
                5.8,
                196,
                "test"
        );
    }
    @Test
    public void testSearchMoviesWithQueryIsTEST() throws Exception {

        Pageable pageable = PageRequest.of(0, 10);
        List<MovieDTO> movieList = Collections.singletonList(movie);
        Page<MovieDTO> moviePage = new PageImpl<>(movieList, pageable, movieList.size());

        // Mock the service method
        Mockito.when(movieServiceImpl.searchMovies(Mockito.anyString(), Mockito.anyInt(), Mockito.anyInt()))
                .thenReturn(moviePage);

        mockMvc.perform(get("/api/movies/search")
                        .param("query", "test")
                        .param("page", "0")
                        .param("size", "10")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].movieId").value(6279))
                .andExpect(jsonPath("$.content[0].title").value("Kraven the Hunter"))
                .andExpect(jsonPath("$.content[0].releaseDate").value("2024-12-11"))
                .andExpect(jsonPath("$.content[0].overview").value("Kraven Kravinoff's complex relationship with his ruthless gangster father, Nikolai, starts him down a path of vengeance with brutal consequences, motivating him to become not only the greatest hunter in the world, but also one of its most feared."))
                .andExpect(jsonPath("$.content[0].genres[?(@.name == 'Thriller')]").exists())
                .andExpect(jsonPath("$.content[0].genres[?(@.name == 'Action')]").exists())
                .andExpect(jsonPath("$.content[0].genres[?(@.name == 'Adventure')]").exists());

    }


    @Test
    // test with query "none" and page 0, size 10 reuturn empty list
    public void testSearchMoviesWithPaginationEmptyList() throws Exception {

        Pageable pageable = PageRequest.of(0, 10);
        List<MovieDTO> movieList = Collections.emptyList();
        Page<MovieDTO> moviePage = new PageImpl<>(movieList, pageable, movieList.size());

        // Mock the service method
        Mockito.when(movieServiceImpl.searchMovies(Mockito.anyString(), Mockito.anyInt(), Mockito.anyInt()))
                .thenReturn(moviePage);

        mockMvc.perform(get("/api/movies/search")
                        .param("query", "none")
                        .param("page", "0")
                        .param("size", "10")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isEmpty());

    }

    @Test
    public void getMovieByIdReturnMovie() throws Exception {
        // Mock the service method
        Mockito.when(movieServiceImpl.getMovieById(6279L))
                .thenReturn(movie);

        mockMvc.perform(get("/api/movies/6279")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.movieId").value(6279))
                .andExpect(jsonPath("$.title").value("Kraven the Hunter"));
    }

    @Test
    public void getMovieByIdReturnNotFound() throws Exception {
        // Mock the service method
        Mockito.when(movieServiceImpl.getMovieById(111L)).thenThrow(new ResourceNotFoundException("Movie not found"));

        mockMvc.perform(get("/api/movies/111")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

    }
}
