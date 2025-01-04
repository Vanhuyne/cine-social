package com.huy.backend.models;

import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Entity
@Table(name = "genres")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Genre {
    @Id
    private Long genreId;

    @Column(nullable = false, length = 100)
    private String name;

    @ManyToMany(mappedBy = "genres")
    private Set<Movie> movies;

    public Genre(Long genreId, String name) {
        this.genreId = genreId;
        this.name = name;
    }
}