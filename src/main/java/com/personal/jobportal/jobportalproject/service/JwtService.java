package com.personal.jobportal.jobportalproject.service;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Date;
public interface JwtService {
    String extractUsername(String token);
    Integer extractUserId(String token);
    Integer extractUserRole(String token);
    Date extractExpiration(String token);
    Boolean validateToken(String token, UserDetails userDetails);
    String generateToken(UserDetails userDetails);
}
