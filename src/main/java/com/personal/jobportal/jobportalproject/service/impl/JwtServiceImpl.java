package com.personal.jobportal.jobportalproject.service.impl;
import com.personal.jobportal.jobportalproject.enums.RoleEnum;
import com.personal.jobportal.jobportalproject.service.JwtService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Function;
@Component
public class JwtServiceImpl implements JwtService {
    @Value("${jwt.secret-key}")
    private String secretKey;
    @Override
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }
    @Override
    public Integer extractUserId(String token) {
        Claims claims = extractAllClaims(token);
        return (Integer) claims.get("userId");
    }
    @Override
    public Integer extractUserRole(String token){
        Claims claims = extractAllClaims(token);
        return (Integer) claims.get("role");
    }
    @Override
    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }
    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSignKey())
                .build().parseSignedClaims(token.replace("Bearer ", "")).getPayload();
    }
    private Boolean isTokenExpired(String token) {
        return !extractExpiration(token).after(new Date());
    }
    @Override
    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }
    @Override
    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new LinkedHashMap<>();
        claims.put("userId", getUserIdFromUserDetails(userDetails));
        claims.put("userName", userDetails.getUsername());
        claims.put("role", getRoleFromUserDetails(userDetails));
        claims.put("emailId", getEmailIdFromUserDetails(userDetails));
        return createToken(claims, userDetails.getUsername());
    }
    private String createToken(Map<String, Object> claims, String username) {
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 24 * 60 * 60 * 1000))
                .signWith(getSignKey())
                .compact();
    }

    private SecretKey getSignKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }
    private Integer getUserIdFromUserDetails(UserDetails userDetails) {
        return ((CustomUserDetails) userDetails).getUserId();
    }
    private RoleEnum getRoleFromUserDetails(UserDetails userDetails) {
        return RoleEnum.values()[((CustomUserDetails) userDetails).getRole()];
    }
    private String getEmailIdFromUserDetails(UserDetails userDetails) {
        return ((CustomUserDetails) userDetails).getEmail();
    }
}
