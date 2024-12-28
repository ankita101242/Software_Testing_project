package com.example.ProsePetal.Services;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {

    // Secret key for signing JWT tokens (must be Base64 encoded for proper usage)
    //    private static final String SECRET_KEY = "tJm2035MahpYJ2lfJqwbVyAk/7S1251okFOw/uXxGEJ4rjJRw9yGvqKUaDtAqC5z";
    private static final String SECRET_KEY = "tJm2035MahpYJ2lfJqwbVyAk/7S1251okFOw/uXxGEJ4rjJRw9yGvqKUaDtAqC5z12345678901234567890123456789012";

    private static final long ACCESS_TOKEN_VALIDITY_SECONDS = 60 * 60 * 24 * 7; // 7 days
    private static final long REFRESH_TOKEN_VALIDITY_SECONDS = 60 * 60 * 24 * 30; // 30 days

    // Extract username from the token
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    // Extract a specific claim from the token
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    // Generate an access token
    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        return generateTokenWithClaims(claims, userDetails.getUsername(), ACCESS_TOKEN_VALIDITY_SECONDS);
    }

    // Generate a refresh token
    public String generateRefreshToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        return generateTokenWithClaims(claims, userDetails.getUsername(), REFRESH_TOKEN_VALIDITY_SECONDS);
    }

    // Validate the token against the UserDetails
    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    // Private helper to generate a token with claims
    private String generateTokenWithClaims(Map<String, Object> claims, String subject, long validitySeconds) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + validitySeconds * 1000))
                .signWith(getSigningKey(), SignatureAlgorithm.HS512)
                .compact();
    }

    // Extract all claims from the token
    private Claims extractAllClaims(String token) {
        try {
            validateTokenStructure(token);
            return Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid or malformed JWT token: " + e.getMessage(), e);
        }
    }

    // Validate token structure to ensure it has the correct format
    private void validateTokenStructure(String token) {
        if (token == null || token.trim().isEmpty()) {
            throw new IllegalArgumentException("Token is null or empty");
        }
        if (token.split("\\.").length != 3) {
            throw new IllegalArgumentException("Token structure is invalid. Expected format: header.payload.signature");
        }
    }

    // Check if the token is expired
    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    // Extract expiration date from the token
    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    // Generate a signing key for the JWT using the secret key
    private Key getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
