package com.storeapi.services;

import com.storeapi.dtos.AddItemToCartRequest;
import com.storeapi.dtos.CartItemDto;
import com.storeapi.entities.Cart;
import com.storeapi.entities.CartItem;
import com.storeapi.entities.Product;
import com.storeapi.mappers.CartMapper;
import com.storeapi.repositories.CartRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class CartService {

  private final CartRepository cartRepository;
  private final ProductService productService;
  private final CartMapper cartMapper;

  public Cart save() {
    Cart cart = new Cart();
    return cartRepository.save(cart);
  }

  @Transactional
  public CartItemDto addItemToCart(UUID cartId, AddItemToCartRequest request) {
    Cart existingCart = cartRepository.findById(cartId).orElseThrow(() -> new RuntimeException("Cart not found"));
    Optional<Product> productOptional = productService.getById(request.getProductId());
    if (productOptional.isEmpty()) throw new RuntimeException("Product not found");

    CartItem cartItem = existingCart.getCartItems().stream()
      .filter(item -> item.getProduct().getId().equals(request.getProductId()))
      .findFirst()
      .orElse(null);

    if (cartItem != null) {
      cartItem.setQuantity(cartItem.getQuantity() + 1);
    } else {
      cartItem = new CartItem();
      cartItem.setProduct(productOptional.get());
      cartItem.setQuantity(1);
      cartItem.addToCart(existingCart);
    }
    cartRepository.save(existingCart);

    return cartMapper.toDto(cartItem);
  }

  public Cart findById(UUID cartId) {
    return cartRepository.findCartWithItemsByCartId(cartId).orElseThrow(() -> new RuntimeException("Cart not found"));
  }
}
