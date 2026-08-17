package com.storeapi.carts;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@Tag(name = "Carts", description = "All cart related endpoints")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/carts")
public class CartController {

  private final CartService cartService;
  private final CartMapper cartMapper;

  @GetMapping
  public ResponseEntity<List<CartDto>> getAllCarts() {
    List<Cart> carts = cartService.findAll();
    List<CartDto> cartDtos = cartMapper.toDtoList(carts);
    return ResponseEntity.ok(cartDtos);
  }

  @GetMapping("{cartId}")
  @Operation(
    summary = "Get cart by ID",
    description = "Retrieve a cart along with its items using cart UUID."
  )
  public ResponseEntity<CartDto> getCart(
    @PathVariable(name = "cartId") UUID cartId
  ) {
    Cart cart = cartService.findById(cartId);
    CartDto cartDto = cartMapper.toDto(cart);
    return ResponseEntity.ok(cartDto);
  }

  @PostMapping
  @Operation(
    summary = "Create new cart",
    description = "Create a new empty cart and return its details."
  )
  public ResponseEntity<CartDto> createCart(
    UriComponentsBuilder uriComponentsBuilder
  ) {
    Cart savedCart = cartService.save();
    CartDto cartDto = cartMapper.toDto(savedCart);

    URI uri = uriComponentsBuilder
      .path("/api/carts/{cartId}")
      .buildAndExpand(cartDto.getId())
      .toUri();

    return ResponseEntity.created(uri).body(cartDto);
  }

  @PostMapping("/{cartId}/items")
  @Operation(
    summary = "Add item to cart",
    description = "Add a product to the cart or increase quantity if already exists."
  )
  public ResponseEntity<CartItemDto> addToCart(
    @PathVariable(name = "cartId") UUID cartId,
    @Valid @RequestBody AddItemToCartRequest request
  ) {
    CartItemDto cartItemDto = cartService.addItemToCart(cartId, request);
    return new ResponseEntity<>(cartItemDto, HttpStatus.CREATED);
  }

  @PutMapping("/{cartId}/items/{productId}")
  @Operation(
    summary = "Update cart item",
    description = "Update quantity of a specific product inside the cart."
  )
  public ResponseEntity<CartItemDto> updateCartItem(
    @PathVariable(name = "cartId") UUID cartId,
    @PathVariable(name = "productId") Long productId,
    @Valid @RequestBody UpdateCartItemRequest request
  ) {
    CartItemDto cartItemDto = cartService.updateCartItem(cartId, productId, request);
    return ResponseEntity.ok(cartItemDto);
  }

  @DeleteMapping("/{cartId}/items/{productId}")
  @Operation(
    summary = "Remove item from cart",
    description = "Delete a specific product from the cart."
  )
  public ResponseEntity<CartItemDto> deleteCartItem(
    @PathVariable(name = "cartId") UUID cartId,
    @PathVariable(name = "productId") Long productId
  ) {
    cartService.deleteCartItem(cartId, productId);
    return ResponseEntity.noContent().build();
  }

  @DeleteMapping("/{cartId}/items")
  @Operation(
    summary = "Clear cart",
    description = "Remove all items from the cart."
  )
  public ResponseEntity<CartItemDto> clearCart(
    @PathVariable(name = "cartId") UUID cartId
  ) {
    cartService.clearCart(cartId);
    return ResponseEntity.noContent().build();
  }
}