package com.storeapi.controllers;

import com.storeapi.dtos.LoginRequest;
import com.storeapi.dtos.LoginResponse;
import com.storeapi.dtos.UserDto;
import com.storeapi.entities.User;
import com.storeapi.mappers.UserMapper;
import com.storeapi.services.JwtService;
import com.storeapi.services.UserServices;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/auth")
public class AuthController {
  private final AuthenticationManager authenticationManager;
  private final JwtService jwtService;
  private final UserServices userServices;
  private final UserMapper userMapper;

  @Value("${spring.jwt.refresh-token-expiration-in-seconds}")
  private int jwtRefreshTokenExpirationInSeconds;

  @PostMapping("/login")
  public ResponseEntity<LoginResponse> login(
    @RequestBody @Valid LoginRequest loginRequest,
    HttpServletResponse response
  ) {
    authenticationManager.authenticate(
      new UsernamePasswordAuthenticationToken(
        loginRequest.getEmail(),
        loginRequest.getPassword()
      )
    );

    var user = userServices.getByEmail(loginRequest.getEmail()).orElseThrow(() -> new UsernameNotFoundException("User not found"));

    String accessToken = jwtService.generateAccessToken(user);
    String refreshToken = jwtService.generateRefreshToken(user);

    var cookie = new Cookie("refresh_token", refreshToken);
    cookie.setHttpOnly(true);
    cookie.setPath("/auth/refresh");
    cookie.setMaxAge(jwtRefreshTokenExpirationInSeconds);
    cookie.setSecure(true);
    response.addCookie(cookie);

    return ResponseEntity.status(HttpStatus.OK).body(new LoginResponse(accessToken));
  }

  @PostMapping("/validate")
  public ResponseEntity<String> validateToken(
    @RequestHeader("Authorization") String authHeader
  ) {
    System.out.println("Validate Called....");
    String jwtToken = authHeader.replace("Bearer ", "");
    boolean result = jwtService.validateToken(jwtToken);
    if (!result) return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid token");
    return ResponseEntity.ok("Token is valid");
  }

  @GetMapping("/me")
  public ResponseEntity<UserDto> getCurrentUser() {
    var authentication = SecurityContextHolder.getContext().getAuthentication();
    assert authentication != null;
    Long id = (Long) authentication.getPrincipal();

    Optional<User> existingUser = userServices.getById(id);
    if (existingUser.isEmpty()) throw new UsernameNotFoundException("User not found");

    var userDto = userMapper.toDto(existingUser.get());
    return ResponseEntity.ok(userDto);
  }
}
