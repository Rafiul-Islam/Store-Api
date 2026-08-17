package com.storeapi.carts;

import com.storeapi.products.Product;
import com.storeapi.products.ProductService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
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
    Cart existingCart = cartRepository.findCartWithItemsByCartId(cartId).orElseThrow(() -> new RuntimeException("Cart not found"));
    Optional<Product> productOptional = productService.getById(request.getProductId());
    if (productOptional.isEmpty()) throw new RuntimeException("Product not found");

    CartItem cartItem = existingCart.addCartItem(productOptional.get());

    cartRepository.save(existingCart);

    return cartMapper.toDto(cartItem);
  }

  public Cart findById(UUID cartId) {
    return cartRepository.findCartWithItemsByCartId(cartId).orElseThrow(() -> new RuntimeException("Cart not found"));
  }

  public CartItemDto updateCartItem(UUID cartId, Long productId, UpdateCartItemRequest request) {
    Cart existingCart = cartRepository.findCartWithItemsByCartId(cartId).orElseThrow(() -> new RuntimeException("Cart not found"));

    CartItem cartItem = existingCart.getCartItem(productId);

    CartItem updatedCartItem = cartMapper.toUpdateEntity(request, cartItem);
    cartRepository.save(existingCart);

    return cartMapper.toDto(updatedCartItem);
  }

  public void deleteCartItem(UUID cartId, Long productId) {
    Cart existingCart = cartRepository.findCartWithItemsByCartId(cartId).orElseThrow(() -> new RuntimeException("Cart not found"));
    existingCart.removeItem(productId);
    cartRepository.save(existingCart);
  }

  public void clearCart(UUID cartId) {
    Cart existingCart = cartRepository.findCartWithItemsByCartId(cartId).orElseThrow(() -> new RuntimeException("Cart not found"));
    existingCart.clearCart();
    cartRepository.save(existingCart);
  }

  public List<Cart> findAll() {
    return cartRepository.findAllWithItems();
  }

  public Optional<Cart> getById(UUID cartId) {
    return cartRepository.findCartWithItemsByCartId(cartId);
  }
}
