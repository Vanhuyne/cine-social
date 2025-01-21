package com.huy.backend.dto.MovieDTO;


import com.huy.backend.dto.GenreDTO;
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
}
