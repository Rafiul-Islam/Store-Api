package com.storeapi.services;

import com.storeapi.entities.User;
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

  @Value("${spring.jwt.access-token-expiration-in-seconds}")
  private Long jwtAccessTokenExpirationInSeconds;

  @Value("${spring.jwt.refresh-token-expiration-in-seconds}")
  private Long jwtRefreshTokenExpirationInSeconds;

  public String generateAccessToken(User user) {
    return generateToken(user, jwtAccessTokenExpirationInSeconds);
  }

  public String generateRefreshToken(User user) {
    return generateToken(user, jwtRefreshTokenExpirationInSeconds);
  }

  private String generateToken(User user, long TokenExpirationInSeconds) {
    return Jwts.builder()
      .setSubject(String.valueOf(user.getId()))
      .claim("email", user.getEmail())
      .claim("name", String.valueOf(user.getName()))
      .issuedAt(new Date())
      .expiration(new Date(System.currentTimeMillis() + 1000 * TokenExpirationInSeconds))
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

  public Long getUserIdFromToken(String token) {
    return Long.valueOf(getClaims(token).getSubject());
  }
}
