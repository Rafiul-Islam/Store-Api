package com.storeapi.auth;

import com.storeapi.users.Role;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.Data;

import javax.crypto.SecretKey;
import java.time.LocalDateTime;

@Data
public class Jwt {
  private final Claims claims;
  private final SecretKey key;

  public Jwt(Claims claims, SecretKey key) {
    this.claims = claims;
    this.key = key;
  }

  public boolean isExpired() {
    return claims.getExpiration().before(new java.util.Date());
  }

  public Long getUserId() {
    return Long.valueOf(claims.getSubject());
  }

  public Role getUserRole() {
    return Role.valueOf(claims.get("role", String.class));
  }

  public String getJti() {
    return claims.getId();
  }

  public LocalDateTime getExpiration() {
    return claims.getExpiration().toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDateTime();
  }

  public String toString() {
    return Jwts.builder()
      .setClaims(claims)
      .signWith(key)
      .compact();
  }
}
