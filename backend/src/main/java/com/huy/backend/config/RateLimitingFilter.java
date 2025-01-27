package com.huy.backend.config;

import io.github.bucket4j.Bucket;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class RateLimitingFilter implements Filter {
    private final Bucket bucket;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        Filter.super.init(filterConfig);
    }

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        if (bucket.tryConsume(1)) {
            chain.doFilter(request, response); // Forward the request if rate limiting is not hit
        } else {
            ((HttpServletResponse) response).setStatus(HttpStatus.TOO_MANY_REQUESTS.value()); // Return 429 if rate limit is exceeded
        }
    }

    @Override
    public void destroy() {
        Filter.super.destroy();
    }
}
