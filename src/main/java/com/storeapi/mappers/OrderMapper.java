package com.storeapi.mappers;

import com.storeapi.dtos.OrderDto;
import com.storeapi.entities.Order;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface OrderMapper {
  @Mapping(target = "items")
  OrderDto toDto(Order order);
  List<OrderDto> toDtoList(List<Order> orders);
}
