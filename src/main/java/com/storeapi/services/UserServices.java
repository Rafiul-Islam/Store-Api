package com.storeapi.services;

import com.storeapi.dtos.ChangePasswordRequest;
import com.storeapi.dtos.RegisterUserRequest;
import com.storeapi.dtos.UpdateUserRequest;
import com.storeapi.entities.User;
import com.storeapi.enums.Role;
import com.storeapi.mappers.UserMapper;
import com.storeapi.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserServices {
  private final UserRepository userRepository;
  private final UserMapper userMapper;
  private final PasswordEncoder passwordEncoder;

  public List<User> findAll(String sortBy) {
    if (!Set.of("name", "email" ).contains(sortBy)) sortBy = "name";
    return userRepository.findAll(Sort.by(sortBy));
  }

  public User findById(long userId) {
    return userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found" ));
  }

  public User save(RegisterUserRequest request) {
    userRepository.findByEmail(request.getEmail()).ifPresent((user) -> {
      throw new RuntimeException("Email is already registered" );
    });
    User user = userMapper.toEntity(request);
    user.setPassword(passwordEncoder.encode(user.getPassword()));
    user.setRole(Role.USER);
    return userRepository.save(user);
  }

  public User update(Long userId, UpdateUserRequest request) {
    User savedUser = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found" ));
    userMapper.updateEntity(request, savedUser);
    return userRepository.save(savedUser);
  }

  public void delete(Long userId) {
    User savedUser = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found" ));
    userRepository.delete(savedUser);
  }

  public Boolean changePassword(Long userId, ChangePasswordRequest request) {
    Optional<User> optionalUser = userRepository.findById(userId);
    if (optionalUser.isEmpty()) {
      return false;
    }
    User user = optionalUser.get();
    if (!user.getPassword().equals(request.getOldPassword())) return false;
    user.setPassword(request.getNewPassword());
    userRepository.save(user);
    return true;
  }

  public Optional<User> getByEmail(String userEmail) {
    return userRepository.findByEmail(userEmail);
  }

  public Optional<User> getById(long userId) {
    return userRepository.findById(userId);
  }

}
