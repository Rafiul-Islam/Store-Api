package com.storeapi.services;

import com.storeapi.configs.JwtConfig;
import com.storeapi.entities.User;
import com.storeapi.utils.Jwt;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;

@RequiredArgsConstructor
@Service
public class JwtService {

  private final JwtConfig jwtConfig;

  public Jwt generateAccessToken(User user) {
    return generateToken(user, Long.parseLong(jwtConfig.getAccessTokenExpirationInSeconds()));
  }

  public Jwt generateRefreshToken(User user) {
    return generateToken(user, Long.parseLong(jwtConfig.getRefreshTokenExpirationInSeconds()));
  }

  private Jwt generateToken(User user, long TokenExpirationInSeconds) {
    var claims = Jwts.claims()
      .setSubject(String.valueOf(user.getId()))
      .add("name", String.valueOf(user.getName()))
      .add("email", user.getEmail())
      .add("role", String.valueOf(user.getRole()))
      .setIssuedAt(new Date())
      .setExpiration(new Date(System.currentTimeMillis() + 1000 * TokenExpirationInSeconds))
      .build();

    return new Jwt(claims, jwtConfig.getSecretKey());
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

  public Jwt parseToken(String token) {
    try {
      var claims = getClaims(token);
      return new Jwt(claims, jwtConfig.getSecretKey());
    } catch (Exception e) {
      return null;
    }
  }
}
