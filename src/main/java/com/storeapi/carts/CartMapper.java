package com.storeapi.carts;

import com.storeapi.products.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CartMapper {
  @Mapping(target = "items" , source = "cartItems")
  CartDto toDto(Cart cart);

  List<CartDto> toDtoList(List<Cart> cart);

  CartProductDto toProductDto(Product product);

  CartItemDto toDto(CartItem cartItem);

  CartItem toUpdateEntity(UpdateCartItemRequest request, @MappingTarget CartItem cartItem);
}

