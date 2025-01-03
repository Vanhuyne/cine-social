package com.huy.imdb_backend.models;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;


@Entity
@Table(name = "movies")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Movie {
    @Id
    private Long movieId;

    @Column(nullable = false , length = 255)
    private String title;

    private LocalDate releaseDate;
    private Integer runtime;

    @Lob
    private String overview;
    private String posterPath;
    private String backdropPath;

    @Column(unique = true)
    private Integer tmdbId;

    private LocalDateTime createdAt;

    // many to many relationship with genres
    @ManyToMany
    @JoinTable(
            name = "movie_genres",
            joinColumns = @JoinColumn(name = "movie_id"),
            inverseJoinColumns = @JoinColumn(name = "genre_id")
    )
    private Set<Genre> genres = new HashSet<>();
    private Double popularity;
    private Double voteAverage;
    private Integer voteCount;
    private String trailerKey;
}
