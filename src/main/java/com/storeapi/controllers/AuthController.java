package com.storeapi.controllers;

import com.storeapi.dtos.LoginRequest;
import com.storeapi.dtos.LoginResponse;
import com.storeapi.services.JwtService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/auth")
public class AuthController {
  private final AuthenticationManager authenticationManager;
  private final JwtService jwtService;

  @PostMapping("/login")
  public ResponseEntity<LoginResponse> login(
    @RequestBody @Valid LoginRequest loginRequest
  ) {
    authenticationManager.authenticate(
      new UsernamePasswordAuthenticationToken(
        loginRequest.getEmail(),
        loginRequest.getPassword()
      )
    );

    String authToken = jwtService.generateToken(loginRequest.getEmail());

    return ResponseEntity.status(HttpStatus.OK).body(new LoginResponse(authToken));
  }
}
