package com.example.greenshadow.service.impl;

import com.example.greenshadow.service.JWTService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
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
public class JWTServiceImpl implements JWTService {

    // JWT secret key injected from application properties
    @Value("${spring.jwtKey}")
    private String jwtKey;

    // Extracts the username (subject) from the JWT token
    @Override
    public String extractUserName(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    // Generates a new JWT token for the provided user details
    @Override
    public String generateToken(UserDetails userDetails) {
        return generateToken(new HashMap<>(),userDetails);
    }

    // Validates the token by checking its username and expiration status
    @Override
    public boolean validateToken(String token, UserDetails userDetails) {
        var username = extractUserName(token);
        return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

    // Refreshes the token by generating a new one with updated expiration
    @Override
    public String refreshToken(UserDetails userDetails) {
        return refreshToken(new HashMap<>(),userDetails);
    }

    // Helper method to extract a specific claim from the token
    private <T> T extractClaim(String token, Function<Claims,T> claimResolve) {
        final Claims claims = getAllClaims(token);
        return claimResolve.apply(claims);
    }

    // Generates a new JWT token with custom claims and user details
    private String generateToken(Map<String,Object> extractClaims, UserDetails userDetails){
        extractClaims.put("role",userDetails.getAuthorities()); // Add user roles as a claim
        Date now = new Date();
        Date expire = new Date(now.getTime() + 15 * 60 * 1000);  // Token expires in 15 minutes

        return Jwts.builder().setClaims(extractClaims) // Set custom claims
                .setSubject(userDetails.getUsername()) // Set username as subject
                .setIssuedAt(now)  // Set token issue time
                .setExpiration(expire) // Set expiration time
                .signWith(getSignKey(), SignatureAlgorithm.HS256).compact(); // Sign the token with HMAC SHA-256 and build the token

    }
    // Generates a refresh token with a longer expiration time
    private String refreshToken(Map<String,Object> extractClaims,UserDetails userDetails){
        extractClaims.put("role",userDetails.getAuthorities());
        Date now = new Date();
        Date expire = new Date(now.getTime() + 15 * 60 * 1000);
        Date refreshExpire = new Date(now.getTime() + 7 * 24 * 60 * 60 * 1000);

        return Jwts.builder().setClaims(extractClaims)
                .setSubject(userDetails.getUsername())
                .setExpiration(refreshExpire)
                .signWith(getSignKey(), SignatureAlgorithm.HS256).compact();
    }

    // Checks if the token is expired
    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }
    // Extracts the expiration date from the token
    private Date extractExpiration(String token) {
        return extractClaim(token,Claims::getExpiration);
    }
    // Parses and retrieves all claims from the token
    private Claims getAllClaims(String token) {
        return Jwts.parser().setSigningKey(getSignKey()).build().parseClaimsJws(token) // Set signing key for validation
                .getBody();
    }

    // Retrieves the signing key from the JWT secret key
    private Key getSignKey(){
        byte[] decode = Decoders.BASE64.decode(jwtKey);  // Decode base64 key
        return Keys.hmacShaKeyFor(decode); // Create HMAC SHA-256 key
    }
}
