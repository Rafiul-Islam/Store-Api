package com.storeapi.mappers;

import com.storeapi.dtos.CartDto;
import com.storeapi.dtos.CartItemDto;
import com.storeapi.dtos.CartProductDto;
import com.storeapi.dtos.UpdateCartItemRequest;
import com.storeapi.entities.Cart;
import com.storeapi.entities.CartItem;
import com.storeapi.entities.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface CartMapper {
  @Mapping(target = "items" , source = "cartItems")
  CartDto toDto(Cart cart);

  CartProductDto toProductDto(Product product);

  CartItemDto toDto(CartItem cartItem);

  CartItem toUpdateEntity(UpdateCartItemRequest request, @MappingTarget CartItem cartItem);
}

