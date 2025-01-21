package com.huy.backend.dto.Movie;

import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class MovieUpdateDTO {
    private Long movieId;

    @NotNull
    private String title;
    private LocalDate releaseDate;
    private Integer runtime;
    private String overview;
    private String posterPath;
    private String backdropPath;

    @NotNull
    private Integer tmdbId;
    private LocalDateTime createdAt;
    private Set<Long> genreIds;
    private Double popularity;
    private Double voteAverage;
    private Integer voteCount;
    private String trailerKey;
}
