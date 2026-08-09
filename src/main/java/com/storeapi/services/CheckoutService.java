package com.storeapi.services;

import com.storeapi.dtos.CheckoutRequest;
import com.storeapi.dtos.CheckoutResponse;
import com.storeapi.entities.Cart;
import com.storeapi.entities.Order;
import com.storeapi.entities.User;
import com.storeapi.exceptions.CartNotFoundException;
import com.storeapi.exceptions.EmptyCartException;
import com.storeapi.exceptions.StripeCheckoutException;
import com.storeapi.exceptions.UserNotFoundException;
import com.storeapi.repositories.OrderRepository;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class CheckoutService {
  private final CartService cartService;
  private final AuthService authService;
  private final OrderRepository orderRepository;

  @Value("${website.url}")
  private String websiteUrl;

  @Transactional
  public CheckoutResponse checkout(CheckoutRequest checkoutRequest) throws StripeException {
    Cart existingCart = cartService.getById(checkoutRequest.getCartId()).orElseThrow(CartNotFoundException::new);
    User loggedInUser = authService.getLoggedInUser().orElseThrow(UserNotFoundException::new);
    if (existingCart.isEmpty()) throw new EmptyCartException();

    Order order = Order.frommCart(existingCart, loggedInUser);
    orderRepository.save(order);

    try {
      SessionCreateParams.Builder stripeSessionBuilder = SessionCreateParams.builder()
        .setMode(SessionCreateParams.Mode.PAYMENT)
        .setSuccessUrl(websiteUrl + "/checkout-success?order" + order.getId())
        .setCancelUrl(websiteUrl + "/checkout-cancel");

      order.getItems().forEach(item -> {
        SessionCreateParams.LineItem lineItem = SessionCreateParams.LineItem.builder()
          .setQuantity((Long.valueOf(item.getQuantity())))
          .setPriceData(
            SessionCreateParams.LineItem.PriceData.builder()
              .setCurrency("usd")
              .setUnitAmountDecimal(item.getUnitPrice().multiply(new java.math.BigDecimal(100)))
              .setProductData(
                SessionCreateParams.LineItem.PriceData.ProductData.builder()
                  .setName(item.getProduct().getName())
                  .build()
              )
              .build()
          )
          .build();

        stripeSessionBuilder.addLineItem(lineItem);
      });

      Session stripeSession = Session.create(stripeSessionBuilder.build());

      cartService.clearCart(existingCart.getId());

      return new CheckoutResponse(order.getId(), stripeSession.getUrl());
    } catch (StripeException stripeException) {
      orderRepository.delete(order);
      throw new StripeCheckoutException(stripeException);
    }
  }
}
