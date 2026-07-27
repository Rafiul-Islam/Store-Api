package com.storeapi.services;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class JwtService {

  @Value("${spring.jwt.secret}")
  private String jwtSecret;

  @Value("${spring.jwt.token-expiration-in-seconds}")
  private Long jwtTokenExpirationInSeconds;

  public String generateToken(String email) {
    return Jwts.builder()
      .setSubject(email)
      .issuedAt(new Date())
      .expiration(new Date(System.currentTimeMillis() + 1000 * jwtTokenExpirationInSeconds))
      .signWith(Keys.hmacShaKeyFor(jwtSecret.getBytes()))
      .compact();
  }

  public boolean validateToken(String token) {
    try {
      var claims = getClaims(token);
      return claims.getExpiration().after(new Date());
    } catch (Exception e) {
      return false;
    }
  }

  private Claims getClaims(String token) {
    return Jwts.parser()
      .verifyWith(Keys.hmacShaKeyFor(jwtSecret.getBytes()))
      .build()
      .parseSignedClaims(token)
      .getPayload();
  }

  public String getEmailFromToken(String token) {
    return getClaims(token).getSubject();
  }
}
