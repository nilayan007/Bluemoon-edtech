package com.bluemoon.bluemoonedtech.config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // disable CSRF for now (we're doing API)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/health").permitAll() // health is open
                        .anyRequest().permitAll() // TEMP: allow all endpoints, we'll tighten later
                );

        return http.build();
    }
}