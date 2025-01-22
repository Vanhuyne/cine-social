package com.huy.backend.client;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MovieSyncScheduler {

    private final MovieClient movieClient;

    /**
     * Sync movies every 10 days at fixed intervals.
     */
    @Scheduled(fixedRate = 10 * 24 * 60 * 60 * 1000) // 10 days in milliseconds
    public void synchronizeFetchMovies() {
//        movieClient.syncMovie();
//        movieClient.updateMoviesWithRuntime();
//        movieClient.getTrailerKey(693134);
//        movieClient.updateTrailerKeys();
    }
}
