package com.example.evaluation_1.signupAndLogin.configuration;

import org.springframework.stereotype.Component;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;

/**
 * Service for handling JWT operations, including generation, validation, and extraction of claims.
 */
@Component
public class JwtService {
    private static final String SECRET_KEY = "Hello@123";

    // Generates a JWT token with the given email as the subject.
    public String generateToken(String email) {
        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60))
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();
    }

    //     Extracts claims from the JWT token.

    public Claims extractClaims(String token) {
        return Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .parseClaimsJws(token)
                .getBody();
    }

    //   Extracts and parses the user ID from the JWT token.
    public Long extractUserId(String token) {
        try {
            String subject = extractClaims(token).getSubject();
            return Long.parseLong(subject);
        } catch (NumberFormatException e) {

            throw new RuntimeException("Invalid user ID in token");
        } catch (Exception e) {

            throw new RuntimeException("Invalid token");
        }
    }

    // Extracts the username from the JWT token.
    public String extractUsername(String token) {
        return extractClaims(token).getSubject();
    }

    //      Checks if the JWT token has expired.
    public boolean isTokenExpired(String token) {
        return extractClaims(token).getExpiration().before(new Date());
    }

    //     Validates the JWT token.
    public boolean validateToken(String token, String username) {
        return (username.equals(extractUsername(token)) && !isTokenExpired(token));
    }
}
