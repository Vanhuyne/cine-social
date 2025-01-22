package com.huy.backend.dto.movie;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDate;
import java.util.Set;

@Getter
@Setter
@Builder
public class MovieUpdateDTO {
    @NotNull(message = "Title is required")
    @Size(min = 1, max = 255, message = "Title must be between 1 and 255 characters")
    private String title;

    @NotNull(message = "Release date is required")
    private LocalDate releaseDate;

    private Integer runtime;

    @NotNull(message = "Overview is required")
    private String overview;

    private String posterPath;
    private String backdropPath;

    @NotNull(message = "TMDB ID is required")
    private Integer tmdbId;

    @NotEmpty(message = "At least one genre is required")
    private Set<Long> genreIds;

    private String trailerKey;
}
