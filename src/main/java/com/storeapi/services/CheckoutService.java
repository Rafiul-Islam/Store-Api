package com.storeapi.services;

import com.storeapi.dtos.CheckoutRequest;
import com.storeapi.dtos.CheckoutResponse;
import com.storeapi.dtos.CheckoutSession;
import com.storeapi.entities.Cart;
import com.storeapi.entities.Order;
import com.storeapi.entities.User;
import com.storeapi.exceptions.CartNotFoundException;
import com.storeapi.exceptions.EmptyCartException;
import com.storeapi.exceptions.PaymentGatewayException;
import com.storeapi.exceptions.UserNotFoundException;
import com.storeapi.repositories.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class CheckoutService {
  private final CartService cartService;
  private final AuthService authService;
  private final OrderRepository orderRepository;
  private final PaymentGateway paymentGateway;

  @Transactional
  public CheckoutResponse checkout(CheckoutRequest checkoutRequest) {
    Cart existingCart = cartService.getById(checkoutRequest.getCartId()).orElseThrow(CartNotFoundException::new);
    User loggedInUser = authService.getLoggedInUser().orElseThrow(UserNotFoundException::new);
    if (existingCart.isEmpty()) throw new EmptyCartException();

    Order order = Order.frommCart(existingCart, loggedInUser);
    orderRepository.save(order);

    try {
      CheckoutSession checkoutSession = paymentGateway.createCheckoutSession(order);
      cartService.clearCart(existingCart.getId());
      return new CheckoutResponse(order.getId(), checkoutSession.getCheckoutUrl());
    } catch (Exception e) {
      orderRepository.delete(order);
      throw new PaymentGatewayException(e.getMessage());
    }
  }
}
