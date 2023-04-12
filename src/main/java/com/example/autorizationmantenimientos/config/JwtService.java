package com.example.autorizationmantenimientos.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {
    @Value("${app.jwt.access.secret}")
    private String ACCESS_TOKEN_SECRET;

    public String extractUsername(String token) {
//        return extractAllClaims(token).getSubject();
        return extractClaim(token, Claims::getSubject);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claims != null
                ? claimsResolver.apply(claims)
                : null;
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return username.equals(userDetails.getUsername()) && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        Date date = extractClaim(token, Claims::getExpiration);
        return date == null || date.before(new Date());
    }

    public String generateToken(UserDetails userDetails) {
        return generateToken(new HashMap<>(), userDetails);
    }

    public String generateToken(Map<String, Object> payload, UserDetails userDetails) {
        // 1 hour
        int ACCESS_TOKEN_EXPIRATION_MILLIS = 1000 * 60 * 60;
        Date expirationDate = new Date(System.currentTimeMillis() + ACCESS_TOKEN_EXPIRATION_MILLIS);
        return Jwts
                .builder()
                .setSubject(userDetails.getUsername())
                .setExpiration(expirationDate)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .addClaims(payload)
                .signWith(getSigningKey())
                .compact();
    }

    private Claims extractAllClaims(String token) {
        try {
            return Jwts
                    .parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

        } catch (ExpiredJwtException e) {
            return null;
        }
    }

    private Key getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(ACCESS_TOKEN_SECRET);
        return Keys.hmacShaKeyFor(keyBytes);
    }

}
