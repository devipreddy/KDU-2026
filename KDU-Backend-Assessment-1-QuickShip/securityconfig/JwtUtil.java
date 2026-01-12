package com.example.QuickShip.securityconfig;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.*;
import java.util.Date;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
public class JwtUtil {

    private final Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);
    private final Logger logger = LoggerFactory.getLogger(JwtUtil.class);

    private static final long EXPIRATION = 15 * 60 * 1000; // 15 minutes

    public String generateToken(String username, List<String> roles) {

        // Create claims object
        Claims claims = Jwts.claims();
        claims.setSubject(username);             // who
        claims.put("roles", roles);              // what they can do
        claims.setIssuedAt(new Date());           // when issued
        claims.setExpiration(new Date(System.currentTimeMillis() + EXPIRATION));  // expiration
        // Sign & build JWT
        logger.info("Token generated successfully for user: {}", username);
        return Jwts.builder()
                .setClaims(claims)
                .signWith(key)
                .compact();
        
    }


    public boolean isValid(String token) {
        try {
            getClaims(token);
            logger.info("Token is valid.");
            return true;
        } catch (JwtException e) {
            logger.error("Invalid token: {}", e.getMessage());
            return false;
        }
    }


    public String getUsername(String token) {
        return getClaims(token).getSubject();
    }

    public List<String> getRoles(String token) {
        return getClaims(token).get("roles", List.class);
    }

    private Claims getClaims(String token) {

        logger.info("Parsing token: {}", token);  
        return Jwts.parserBuilder()   // Create JWT parser for reading claims and verifying signature
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
        
        
    }
}
