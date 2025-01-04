package com.huy.backend.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class GenreDTO {
    private Long genreId;
    private String name;
}
