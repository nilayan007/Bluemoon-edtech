package com.bluemoon.bluemoonedtech.security.ratelimit;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.Duration;
import java.util.Map;

/**
 * Throttles the public, pre-auth endpoints that are cheapest to abuse:
 * login (credential stuffing), register/OTP endpoints (spam + cost since each
 * OTP triggers a SendGrid email). Keyed by client IP + endpoint, using a
 * token-bucket per key (see RateLimiterService).
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class RateLimitingFilter extends OncePerRequestFilter {

    private record Rule(int capacity, Duration refillPeriod) {}

    private static final Map<String, Rule> RULES = Map.of(
            "/api/auth/login", new Rule(5, Duration.ofMinutes(1)),
            "/api/auth/register", new Rule(5, Duration.ofMinutes(10)),
            "/api/auth/forgot-password", new Rule(3, Duration.ofMinutes(5)),
            "/api/auth/verify-forgot-otp", new Rule(5, Duration.ofMinutes(5)),
            "/api/auth/reset-password", new Rule(5, Duration.ofMinutes(5))
    );

    private final RateLimiterService rateLimiterService;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        Rule rule = RULES.get(request.getRequestURI());

        if (rule == null) {
            filterChain.doFilter(request, response);
            return;
        }

        String key = request.getRequestURI() + ":" + resolveClientIp(request);

        if (rateLimiterService.tryConsume(key, rule.capacity(), rule.refillPeriod())) {
            filterChain.doFilter(request, response);
            return;
        }

        log.warn("Rate limit exceeded for {}", key);
        response.setStatus(429); // HttpServletResponse has no TOO_MANY_REQUESTS constant
        response.setContentType("application/json");
        response.getWriter().write("{\"error\":\"Too many requests, please try again later\"}");
    }

    private String resolveClientIp(HttpServletRequest request) {
        String forwardedFor = request.getHeader("X-Forwarded-For");
        if (forwardedFor != null && !forwardedFor.isBlank()) {
            return forwardedFor.split(",")[0].trim();
        }
        return request.getRemoteAddr();
    }
}
