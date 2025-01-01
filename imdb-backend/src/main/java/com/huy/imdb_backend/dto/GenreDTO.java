package com.huy.imdb_backend.dto;

import lombok.*;

import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class GenreDTO {
    private Long genreId;
    private String name;
}
