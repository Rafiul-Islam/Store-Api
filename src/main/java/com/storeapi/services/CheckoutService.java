package com.storeapi.services;

import com.storeapi.dtos.CheckoutRequest;
import com.storeapi.entities.Cart;
import com.storeapi.entities.Order;
import com.storeapi.entities.OrderItem;
import com.storeapi.entities.User;
import com.storeapi.enums.OrderStatus;
import com.storeapi.repositories.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class CheckoutService {
  private final CartService cartService;
  private final AuthService authService;
  private final OrderRepository orderRepository;

  public long checkout(CheckoutRequest checkoutRequest) {
    Optional<Cart> existingCart = cartService.getById(checkoutRequest.getCartId());
    if (existingCart.isEmpty()) throw new RuntimeException("Cart not found");

    User loggedInUser = authService.getLoggedInUser().orElseThrow(() -> new RuntimeException("User not found"));

    Order order = new Order();
    order.setTotalPrice(existingCart.get().getTotal());
    order.setCustomer(loggedInUser);
    order.setStatus(OrderStatus.PENDING);

    if (existingCart.get().getCartItems().isEmpty()) throw new RuntimeException("Cart is empty");

    existingCart.get().getCartItems().forEach(cartItem -> {
      OrderItem orderItem = new OrderItem();
      orderItem.setQuantity(cartItem.getQuantity());
      orderItem.setProduct(cartItem.getProduct());
      orderItem.setTotalPrice(cartItem.getTotalPrice());
      orderItem.setUnitPrice(cartItem.getProduct().getPrice());
      order.addOrderItem(orderItem);
    });

    orderRepository.save(order);

    cartService.clearCart(existingCart.get().getId());

    return order.getId();
  }
}
