package com.storeapi.utils;

import com.storeapi.enums.Role;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.Data;

import javax.crypto.SecretKey;

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

  public String toString() {
    return Jwts.builder()
      .setClaims(claims)
      .signWith(key)
      .compact();
  }
}
