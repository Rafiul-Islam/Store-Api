package com.storeapi.payments;

import com.storeapi.auth.AuthService;
import com.storeapi.carts.Cart;
import com.storeapi.carts.CartService;
import com.storeapi.orders.Order;
import com.storeapi.users.User;
import com.storeapi.carts.CartNotFoundException;
import com.storeapi.carts.CartEmptyException;
import com.storeapi.users.UserNotFoundException;
import com.storeapi.repositories.OrderRepository;
import com.storeapi.services.*;
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
  private final OrderService orderService;
  private final StripePaymentGateway stripePaymentGateway;

  @Transactional
  public CheckoutResponse checkout(CheckoutRequest checkoutRequest) {
    Cart existingCart = cartService.getById(checkoutRequest.getCartId()).orElseThrow(CartNotFoundException::new);
    User loggedInUser = authService.getLoggedInUser().orElseThrow(UserNotFoundException::new);
    if (existingCart.isEmpty()) throw new CartEmptyException();

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

  public void handleWebHookEvent(WebhookRequest request) {
    stripePaymentGateway.parseWebhookRequest(request)
      .ifPresent(paymentResult -> {
        orderService.getById(paymentResult.getOrderId()).ifPresent(order -> {
          order.setStatus(paymentResult.getPaymentStatus());
          orderService.save(order);
        });
      });
  }
}
