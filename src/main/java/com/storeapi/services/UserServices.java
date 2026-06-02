package com.storeapi.services;

import com.storeapi.entities.User;
import com.storeapi.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServices {
  private final UserRepository userRepository;

  public List<User> getUsers(String sort) {
    return userRepository.findAll(Sort.by(sort));
  }

  public Optional<User> getUserById(long id) {
    return userRepository.findById(id);
  }
}
