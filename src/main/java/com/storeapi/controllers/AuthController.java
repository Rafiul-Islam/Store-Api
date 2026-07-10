package com.storeapi.controllers;

import com.storeapi.dtos.LoginRequest;
import com.storeapi.services.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/auth")
public class AuthController {
  private final AuthService authService;

  @PostMapping("/login")
  public ResponseEntity<Void> login(
    @RequestBody @Valid LoginRequest loginRequest
  ) {
    boolean result = authService.login(loginRequest);
    if (!result) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    return ResponseEntity.ok().build();
  }
}
