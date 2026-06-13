package com.storeapi.controllers;

import com.storeapi.dtos.AddItemToCartRequest;
import com.storeapi.dtos.CartDto;
import com.storeapi.dtos.CartItemDto;
import com.storeapi.dtos.UpdateCartItemRequest;
import com.storeapi.entities.Cart;
import com.storeapi.mappers.CartMapper;
import com.storeapi.services.CartService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/carts")
public class CartController {
  private final CartService cartService;
  private final CartMapper cartMapper;

  @GetMapping("{cartId}")
  public ResponseEntity<CartDto> getCart(
    @PathVariable(name = "cartId") UUID cartId
  ) {
    Cart cart = cartService.findById(cartId);
    CartDto cartDto = cartMapper.toDto(cart);
    return ResponseEntity.ok(cartDto);
  }

  @PostMapping
  public ResponseEntity<CartDto> createCart(
    UriComponentsBuilder uriComponentsBuilder
  ) {
    Cart savedCart = cartService.save();
    CartDto cartDto = cartMapper.toDto(savedCart);
    URI uri = uriComponentsBuilder.path("/api/carts/{cartId}").buildAndExpand(cartDto.getId()).toUri();
    return ResponseEntity.created(uri).body(cartDto);
  }

  @PostMapping("/{cartId}/items")
  public ResponseEntity<CartItemDto> addToCart(
    @PathVariable(name = "cartId") UUID cartId,
    @Valid @RequestBody AddItemToCartRequest request
  ) {
    CartItemDto cartItemDto = cartService.addItemToCart(cartId, request);
    return new ResponseEntity<>(cartItemDto, HttpStatus.CREATED);
  }

  @PutMapping("/{cartId}/items/{productId}")
  public ResponseEntity<CartItemDto> updateCartItem(
    @PathVariable(name = "cartId") UUID cartId,
    @PathVariable(name = "productId") Long productId,
    @Valid @RequestBody UpdateCartItemRequest request
  ) {
    CartItemDto cartItemDto = cartService.updateCartItem(cartId, productId, request);
    return new ResponseEntity<>(cartItemDto, HttpStatus.OK);
  }

  @DeleteMapping("/{cartId}/items/{productId}")
  public ResponseEntity<CartItemDto> deleteCartItem(
    @PathVariable(name = "cartId") UUID cartId,
    @PathVariable(name = "productId") Long productId
  ) {
    cartService.deleteCartItem(cartId, productId);
    return ResponseEntity.noContent().build();
  }

  @DeleteMapping("/{cartId}/items")
  public ResponseEntity<CartItemDto> clearCart(
    @PathVariable(name = "cartId") UUID cartId
  ) {
    cartService.clearCart(cartId);
    return ResponseEntity.noContent().build();
  }
}
