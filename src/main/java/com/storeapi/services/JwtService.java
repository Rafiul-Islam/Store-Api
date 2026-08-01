package com.storeapi.services;

import com.storeapi.configs.JwtConfig;
import com.storeapi.entities.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;

@RequiredArgsConstructor
@Service
public class JwtService {

   private final JwtConfig jwtConfig;

  public String generateAccessToken(User user) {
    return generateToken(user, Long.parseLong(jwtConfig.getAccessTokenExpirationInSeconds()));
  }

  public String generateRefreshToken(User user) {
    return generateToken(user, Long.parseLong(jwtConfig.getRefreshTokenExpirationInSeconds()));
  }

  private String generateToken(User user, long TokenExpirationInSeconds) {
    return Jwts.builder()
      .setSubject(String.valueOf(user.getId()))
      .claim("email", user.getEmail())
      .claim("name", String.valueOf(user.getName()))
      .claim("role", String.valueOf(user.getRole()))
      .issuedAt(new Date())
      .expiration(new Date(System.currentTimeMillis() + 1000 * TokenExpirationInSeconds))
      .signWith(jwtConfig.getSecretKey())
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
      .verifyWith(jwtConfig.getSecretKey())
      .build()
      .parseSignedClaims(token)
      .getPayload();
  }

  public Long getUserIdFromToken(String token) {
    return Long.valueOf(getClaims(token).getSubject());
  }

  public String getUserRoleFromToken(String token) {
    return getClaims(token).get("role", String.class);
  }
}
