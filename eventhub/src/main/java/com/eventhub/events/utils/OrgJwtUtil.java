package com.eventhub.events.utils;

import com.eventhub.events.model.Organizations;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.function.Function;

@Component
public class OrgJwtUtil {
    private static final String SECRET_KEY = "The Secret Key";
    private static final long EXPIRATION_TIME = 1000 * 60 * 60;

    public String extractOrganizationName(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .parseClaimsJws(token)
                .getBody();
    }

    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    public String generateToken(Organizations organizations) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("email", organizations.getEmail1());
        claims.put("organizationId", organizations.getOrganizationId());
        return createToken(claims, organizations.getOrganizationName());
    }

    private String createToken(Map<String, Object> claims, String subject) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();
    }

    private Boolean validateToken(String token, Organizations organizations) {
        final String organizationName = extractOrganizationName(token);
        return (organizationName.equals(organizations.getOrganizationName()) && !isTokenExpired(token));
    }
}
