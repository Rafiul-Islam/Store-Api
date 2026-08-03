package com.storeapi.services;

import com.storeapi.dtos.CheckoutRequest;
import com.storeapi.entities.Cart;
import com.storeapi.entities.Order;
import com.storeapi.entities.User;
import com.storeapi.repositories.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CheckoutService {
  private final CartService cartService;
  private final AuthService authService;
  private final OrderRepository orderRepository;

  public long checkout(CheckoutRequest checkoutRequest) {
    Cart existingCart = cartService.getById(checkoutRequest.getCartId()).orElseThrow(() -> new RuntimeException("Cart not found"));
    User loggedInUser = authService.getLoggedInUser().orElseThrow(() -> new RuntimeException("User not found"));

    Order order = Order.frommCart(existingCart, loggedInUser);
    orderRepository.save(order);
    cartService.clearCart(existingCart.getId());

    return order.getId();
  }
}
