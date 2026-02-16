package com.example.Blogging_platform2.securityconfig7;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

@Component
public class JwtUtil {
    // Token expiry in 1 hour
    private static final long EXPIRATION_TIME = 1000 * 60 * 60;

    // Generate a secure random key at runtime
    private final Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);

    // Generate token
    public String generateToken(String username, String roles) {
        return Jwts.builder()
                .setSubject(username) // subject claim
                .claim("roles", roles) // custom claim
                .setIssuedAt(new Date()) // issue time
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    // Extract username
    public String extractUsername(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    // Validate token
    public boolean validateToken(String token, String username) {
        String extractedUsername = extractUsername(token);
        return (extractedUsername.equals(username) && !isTokenExpired(token));
    }

    private boolean isTokenExpired(String token) {
        Date expiration = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getExpiration();
        return expiration.before(new Date());
    }
}
