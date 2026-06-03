package com.storeapi.controllers;

import com.storeapi.dtos.RegisterUserRequest;
import com.storeapi.dtos.UserDto;
import com.storeapi.entities.User;
import com.storeapi.mappers.UserMapper;
import com.storeapi.services.UserServices;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/users")
public class UserController {

  private final UserServices userServices;
  private final UserMapper userMapper;

  @GetMapping
  private List<UserDto> getUsers(
    @RequestParam(required = false, defaultValue = "", name = "sort") String sortBy
  ) {
    if (!Set.of("name", "email").contains(sortBy)) sortBy = "name";
    List<User> users = userServices.getUsers(sortBy);
    return userMapper.toDtoList(users);
  }

  @GetMapping("/{userId}")
  private ResponseEntity<UserDto> getUserById(
    @PathVariable(name = "userId") Long userId
  ) {
    Optional<User> optionalUser = userServices.getUserById(userId);
    if (optionalUser.isPresent()) {
      User user = optionalUser.get();
      UserDto userDto = userMapper.toDto(user);
      return ResponseEntity.ok(userDto);
    }
    return ResponseEntity.notFound().build();
  }

  @PostMapping
  private ResponseEntity<UserDto> createUser(
    @RequestBody RegisterUserRequest request,
    UriComponentsBuilder uriComponentsBuilder
  ){
    Optional<User> optionalUser = userServices.getUserByEmail(request.getEmail());
    if (optionalUser.isPresent()) {
      return ResponseEntity.badRequest().build();
    }
    User savedUser = userServices.saveUser(request);
    UserDto userDto = userMapper.toDto(savedUser);

    URI uri = uriComponentsBuilder.path("/api/users/{userId}" ).buildAndExpand(savedUser.getId()).toUri();
    return ResponseEntity.created(uri).body(userDto);
  }
}
