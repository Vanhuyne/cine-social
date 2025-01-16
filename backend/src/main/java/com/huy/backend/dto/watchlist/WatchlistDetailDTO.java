package com.huy.backend.dto.watchlist;

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
public class WatchlistDetailDTO {
    private Long watchlistId;
    private String name;
    private Long userId;
    private List<MovieWatchListDTO> movies;
    private LocalDateTime createdAt;
}
