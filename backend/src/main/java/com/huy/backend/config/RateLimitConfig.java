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
        // 100 requests per minute, refilling 10 tokens every 6 seconds
        Bandwidth limit = Bandwidth.classic(100, Refill.intervally(10, Duration.ofSeconds(6)));
        return Bucket.builder().addLimit(limit).build();
    }
}
