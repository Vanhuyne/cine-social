package com.huy.backend.dto.watchlist;

import com.huy.backend.dto.MovieDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WatchlistDTO {
    private Long watchlistId;
    private String name;
    private Long userId;
    private List<MovieDTO> movies;
    private LocalDateTime createdAt;
}
