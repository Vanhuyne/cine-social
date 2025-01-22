package com.huy.backend.dto.movie;

import com.huy.backend.models.Genre;
import com.huy.backend.models.Movie;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
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
public class MovieCreateDTO {
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

    public static Movie convertToMovie(MovieCreateDTO movieDTO) {
        return Movie.builder()
                .title(movieDTO.getTitle())
                .releaseDate(movieDTO.getReleaseDate())
                .runtime(movieDTO.getRuntime())
                .overview(movieDTO.getOverview())
                .posterPath(movieDTO.getPosterPath())
                .backdropPath(movieDTO.getBackdropPath())
                .tmdbId(movieDTO.getTmdbId())
                .createdAt(LocalDateTime.now())
                .genres(movieDTO.getGenreIds()
                        .stream()
                        .map(genreId -> Genre.builder().genreId(genreId).build()
                        )
                        .collect(Collectors.toSet())
                )
                .trailerKey(movieDTO.getTrailerKey())
                .build();
    }


}
