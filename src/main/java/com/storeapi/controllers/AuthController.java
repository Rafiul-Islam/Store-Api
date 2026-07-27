package com.storeapi.controllers;

import com.storeapi.dtos.LoginRequest;
import com.storeapi.dtos.LoginResponse;
import com.storeapi.dtos.UserDto;
import com.storeapi.entities.User;
import com.storeapi.mappers.UserMapper;
import com.storeapi.services.JwtService;
import com.storeapi.services.UserServices;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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

    var user = userServices.getByEmail(loginRequest.getEmail()).orElseThrow(() -> new UsernameNotFoundException("User not found"));

    String authToken = jwtService.generateToken(user);

    return ResponseEntity.status(HttpStatus.OK).body(new LoginResponse(authToken));
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
