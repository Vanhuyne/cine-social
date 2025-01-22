package com.huy.backend.dto;

import com.huy.backend.models.Genre;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class GenreDTO {
    private Long genreId;
    private String name;

    public static GenreDTO convertToGenreDTO(Genre genre) {
        return GenreDTO.builder()
                .genreId(genre.getGenreId())
                .name(genre.getName())
                .build();
    }
    public static Genre convertToGenre(GenreDTO genreDTO) {
        return Genre.builder()
                .genreId(genreDTO.getGenreId())
                .name(genreDTO.getName())
                .build();
    }
}
