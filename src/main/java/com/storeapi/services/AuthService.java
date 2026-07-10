package com.storeapi.services;

import com.storeapi.dtos.LoginRequest;
import com.storeapi.entities.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class AuthService {
  private final UserServices userServices;
  private final PasswordEncoder passwordEncoder;

  public boolean login(LoginRequest loginRequest) {
    Optional<User> user = userServices.getByEmail(loginRequest.getEmail());
    if (user.isPresent()) {
      return passwordEncoder.matches(loginRequest.getPassword(), user.get().getPassword());
    }
    return false;
  }
}
