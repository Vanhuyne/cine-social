package com.huy.backend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class AppConfig {

    // config rest template
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
