package com.maverick.jwt;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtTokenService {
    private static final String secret = "1234567890098765432123456789009876543211234567890";

    private static Claims getAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private static Key getSigningKey() {
        return Keys.hmacShaKeyFor(secret.getBytes());
    }

    private static <T> T getClaim(Function<Claims, T> function, String token) {
        return function.apply(getAllClaims(token));
    }

    public String getUsername(String token) {
        return getClaim(Claims::getSubject, token);
    }

    public String getToken(UserDetails userDetails) {
        return getToken(userDetails, new HashMap<>());
    }

    public String getToken(UserDetails userDetails, Map<String, Object> claims) {
        return Jwts.builder().setClaims(claims).setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public Boolean isTokenValid(String token, String username) {
        return (getUsername(token).equals(username) && !isTokenExpired(token));
    }

    private static Boolean isTokenExpired(String token) {
        return (getExpiration(token).before(new Date()));
    }

    private static Date getExpiration(String token) {
        return getClaim(Claims::getExpiration, token);
    }
}