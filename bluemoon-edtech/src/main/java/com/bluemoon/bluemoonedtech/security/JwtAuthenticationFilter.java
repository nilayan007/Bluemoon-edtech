package com.bluemoon.bluemoonedtech.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final String USER_ID = "userId";

    private final JwtUtils jwtUtils;
    private final CustomUserDetailsService userDetailsService;

    public JwtAuthenticationFilter(JwtUtils jwtUtils,
                                   CustomUserDetailsService userDetailsService) {
        this.jwtUtils = jwtUtils;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        final String authHeader = request.getHeader("Authorization");
        String token = null;

        //  Extract token
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            token = authHeader.substring(7);
        }

        try {
            //  Validate and authenticate
            if (token != null
                    && jwtUtils.validateToken(token)
                    && SecurityContextHolder.getContext().getAuthentication() == null) {

                String subject = jwtUtils.getSubject(token); // publicId or username

                UserDetails userDetails =
                        userDetailsService.loadUserByUsername(subject);

                // 🔹 Add userId to logs (MDC)
                MDC.put(USER_ID, subject);

                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(
                                userDetails,
                                null,
                                userDetails.getAuthorities()
                        );

                authToken.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request)
                );

                // Set authentication
                SecurityContextHolder.getContext().setAuthentication(authToken);

                log.debug("JWT authentication successful for user: {}", subject);
            }

        } catch (Exception e) {
            log.error("JWT authentication failed: {}", e.getMessage());
        } finally {
            //  VERY IMPORTANT: clear MDC to prevent leakage
            MDC.remove(USER_ID);
        }

        // Continue filter chain
        filterChain.doFilter(request, response);
    }
}