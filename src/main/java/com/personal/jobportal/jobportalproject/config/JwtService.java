package com.personal.jobportal.jobportalproject.config;
import com.personal.jobportal.jobportalproject.enums.RoleEnum;
import com.personal.jobportal.jobportalproject.service.impl.CustomUserDetails;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.*;
import java.util.function.Function;
@Service
public class JwtService {
    private static final String SECRET = "cfdde95671faf4db502743d2f080a6c83ebed73a696d395c3f9bdd9b1900fee6";
    public String createToken(Map<String, Object> claims){
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 24 * 60 * 60 * 1000))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }
    private Key getSigningKey(){
        byte[] keyBytes = Decoders.BASE64.decode(SECRET);
        return Keys.hmacShaKeyFor(keyBytes);
    }
    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new LinkedHashMap<>();
        claims.put("userId", getUserIdFromUserDetails(userDetails));
        claims.put("userName", userDetails.getUsername());
        claims.put("role", getRoleFromUserDetails(userDetails));
        claims.put("emailId", getEmailIdFromUserDetails(userDetails));
        claims.put("authorities", populateAuthorities(userDetails.getAuthorities()));
        return createToken(claims);
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
    private String populateAuthorities(Collection<? extends GrantedAuthority> authorities){
        Set<String> authoritiesSet = new HashSet<>();
        for(GrantedAuthority authority: authorities){
            authoritiesSet.add(authority.getAuthority());
        }
        return String.join(",",authoritiesSet);
    }
    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey()).build()
                .parseClaimsJws(token).getBody();
    }
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver){
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }
    public String extractEmail(String token) {
        return extractClaim(token, claims -> claims.get("email", String.class));
    }
    public  boolean isTokenValid(String token, UserDetails userDetails){
        final String userName = extractEmail(token);
        return (userName.equals(userDetails.getUsername()));
    }
    public Integer extractUserRole(String token){
        return extractClaim(token, claims -> claims.get("role", Integer.class));
    }
}
