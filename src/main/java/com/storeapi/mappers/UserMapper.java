package com.storeapi.mappers;

import com.storeapi.dtos.UserDto;
import com.storeapi.entities.User;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {
  UserDto toDto(User user);
  List<UserDto> toDtoList(List<User> users);
}
