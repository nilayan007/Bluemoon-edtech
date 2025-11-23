package com.bluemoon.bluemoonedtech.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.Map;

@Service
public class JwtService {

    // Put secret in application.yml or env vars in real apps
    private final Key key = Keys.hmacShaKeyFor("very-very-secret-key-should-be-512-bit-long-please-change".getBytes());
    private final long expirationMillis = 1000L * 60 * 60 * 24; // 24h

    public String generateToken(String subject, Map<String, Object> claims) {
        Date now = new Date();
        Date exp = new Date(now.getTime() + expirationMillis);
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)       // e.g. user's publicId or email
                .setIssuedAt(now)
                .setExpiration(exp)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public Jws<Claims> parseToken(String token) throws JwtException {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token);
    }

    public boolean isTokenValid(String token) {
        try {
            parseToken(token);
            return true;
        } catch (JwtException ex) {
            return false;
        }
    }
}
