package com.huy.backend.client.mapper;

import com.huy.backend.dto.GenreDTO;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class GenreMapper {
    private static final Map<Long, String> GENRE_MAP = new HashMap<>();
    static {
        GENRE_MAP.put(28L, "Action");
        GENRE_MAP.put(14L, "Fantasy");
        GENRE_MAP.put(35L, "Comedy");
        GENRE_MAP.put(18L, "Drama");
        GENRE_MAP.put(12L, "Adventure");
        GENRE_MAP.put(16L, "Animation");
        GENRE_MAP.put(80L, "Crime");
        GENRE_MAP.put(99L, "Documentary");
        GENRE_MAP.put(10751L, "Family");
        GENRE_MAP.put(36L, "History");
        GENRE_MAP.put(27L, "Horror");
        GENRE_MAP.put(10402L, "Music");
        GENRE_MAP.put(9648L, "Mystery");
        GENRE_MAP.put(10749L, "Romance");
        GENRE_MAP.put(878L, "Science Fiction");
        GENRE_MAP.put(10770L, "TV Movie");
        GENRE_MAP.put(53L, "Thriller");
        GENRE_MAP.put(10752L, "War");
        GENRE_MAP.put(37L, "Western");
        // Add more mappings as needed
    }

    public static Set<GenreDTO> mapGenreIdsToDTOs(Set<Long> genreIds) {
        return genreIds.stream()
                .map(id -> GenreDTO.builder()
                        .genreId(id)
                        .name(GENRE_MAP.get(id))
                        .build())
                .collect(Collectors.toSet());
    }
}
