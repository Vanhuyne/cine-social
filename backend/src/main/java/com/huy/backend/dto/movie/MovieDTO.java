package com.huy.backend.dto.movie;


import com.huy.backend.dto.GenreDTO;
import com.huy.backend.models.Movie;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class MovieDTO {
    private Long movieId;

    @NotNull
    private String title;
    private LocalDate releaseDate;
    private Integer runtime;
    private String overview;
    private String posterPath;
    private String backdropPath;
    private Integer tmdbId;
    private LocalDateTime createdAt;
    private Set<GenreDTO> genres;
    private Double popularity;
    private Double voteAverage;
    private Integer voteCount;
    private String trailerKey;

    public static MovieDTO convertToDTO(Movie movie) {
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

    public static Movie convertToMovie(MovieDTO movieDTO) {
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
}
