package com.huy.backend.config;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Refill;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Configuration
public class RateLimitConfig {

    @Bean
    public Bucket createNewBucket() {
        Bandwidth limit = Bandwidth.classic(10, Refill.greedy(5, Duration.ofMinutes(1)));
        return Bucket.builder().addLimit(limit).build();
    }
}
