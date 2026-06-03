package com.storeapi.services;

import com.storeapi.dtos.ChangePasswordRequest;
import com.storeapi.dtos.RegisterUserRequest;
import com.storeapi.dtos.UpdateUserRequest;
import com.storeapi.entities.User;
import com.storeapi.mappers.UserMapper;
import com.storeapi.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServices {
  private final UserRepository userRepository;
  private final UserMapper userMapper;

  public List<User> getUsers(String sort) {
    return userRepository.findAll(Sort.by(sort));
  }

  public Optional<User> getUserById(long id) {
    return userRepository.findById(id);
  }

  public Optional<User> getUserByEmail(String email) {
    return userRepository.findByEmail(email);
  }

  public User saveUser(RegisterUserRequest request) {
    User user = userMapper.toEntity(request);
    return userRepository.save(user);
  }

  public User updateUser(UpdateUserRequest request, User user) {
    userMapper.updateEntity(request, user);
    return userRepository.save(user);
  }

  public void deleteUser(Long userId) {
    userRepository.deleteById(userId);
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
}
