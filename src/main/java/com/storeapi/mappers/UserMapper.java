package com.storeapi.mappers;

import com.storeapi.dtos.RegisterUserRequest;
import com.storeapi.dtos.UpdateUserRequest;
import com.storeapi.dtos.UserDto;
import com.storeapi.entities.User;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {
  UserDto toDto(User user);
  List<UserDto> toDtoList(List<User> users);
  User toEntity(RegisterUserRequest request);
  void updateEntity(UpdateUserRequest request, @MappingTarget User user);
}
