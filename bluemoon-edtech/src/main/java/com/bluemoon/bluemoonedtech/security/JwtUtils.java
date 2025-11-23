package com.bluemoon.bluemoonedtech.security;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


import java.security.Key;
import java.util.Date;

@Component
public class JwtUtils {


    private final Key key;
    private final long expirationMs;


    public JwtUtils(@Value("${jwt.secret}") String secret,
                    @Value("${jwt.expiration-ms}") long expirationMs) {
// secret should be base64 or long enough; decode if base64-like, else use bytes
        byte[] keyBytes;
        if (secret.trim().matches("^[A-Za-z0-9+/=\\s]+$")) {
// likely base64
            keyBytes = Decoders.BASE64.decode(secret.trim());
        } else {
            keyBytes = secret.getBytes();
        }
        this.key = Keys.hmacShaKeyFor(keyBytes);
        this.expirationMs = expirationMs;
    }


    public String generateToken(String subject) {
        Date now = new Date();
        Date exp = new Date(now.getTime() + expirationMs);
        return Jwts.builder()
                .setSubject(subject)
                .setIssuedAt(now)
                .setExpiration(exp)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }


    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (Exception ex) {
            return false;
        }
    }


    public String getSubject(String token) {
        Claims claims = Jwts.parserBuilder().setSigningKey(key).build()
                .parseClaimsJws(token).getBody();
        return claims.getSubject();
    }
}