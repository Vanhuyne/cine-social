package com.huy.imdb_backend.controller;

import com.huy.imdb_backend.dto.GenreDTO;
import com.huy.imdb_backend.dto.MovieDTO;
import com.huy.imdb_backend.service.impl.MovieServiceImpl;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@AutoConfigureMockMvc
@SpringBootTest
public class MovieControllerTest {

    // init mock mvc
     @Autowired
     private MockMvc mockMvc;

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
    }
    @Test
    public void testSearchMoviesWithPagination() throws Exception {

        Pageable pageable = PageRequest.of(0, 10);
        List<MovieDTO> movieList = Collections.singletonList(movie);
        Page<MovieDTO> moviePage = new PageImpl<>(movieList, pageable, 8);

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
                .andExpect(jsonPath("$.content[0].genres[?(@.name == 'Adventure')]").exists())
                .andExpect(jsonPath("$.content[0].popularity").value(1133.345))
                .andExpect(jsonPath("$.content[0].voteAverage").value(5.8))
                .andExpect(jsonPath("$.content[0].voteCount").value(196));

    }
}
