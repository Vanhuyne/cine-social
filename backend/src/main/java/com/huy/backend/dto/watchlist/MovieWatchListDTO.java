package com.huy.backend.dto.watchlist;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MovieWatchListDetailDTO {
    private Long movieId;
    private String title;
    private String posterPath;
    private LocalDate releaseDate;
    private Integer runtime;

}
