package com.storeapi.orders;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface OrderMapper {
  @Mapping(target = "items")
  OrderDto toDto(Order order);
  List<OrderDto> toDtoList(List<Order> orders);
}
