package com.storeapi.services;

import com.storeapi.dtos.CheckoutSession;
import com.storeapi.entities.Order;
import com.storeapi.entities.OrderItem;
import com.storeapi.exceptions.PaymentGatewayException;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class StripePaymentGateway implements PaymentGateway {

  @Value("${website.url}")
  private String websiteUrl;

  @Override
  public CheckoutSession createCheckoutSession(Order order) {
    try {
      SessionCreateParams.Builder stripeSessionBuilder = SessionCreateParams.builder()
        .setMode(SessionCreateParams.Mode.PAYMENT)
        .setSuccessUrl(websiteUrl + "/checkout-success?order" + order.getId())
        .setCancelUrl(websiteUrl + "/checkout-cancel");

      order.getItems().forEach(item -> {
        SessionCreateParams.LineItem lineItem = getLineItem(item);
        stripeSessionBuilder.addLineItem(lineItem);
      });

      Session stripeSession = Session.create(stripeSessionBuilder.build());

      return new CheckoutSession(stripeSession.getUrl());
    } catch (StripeException e) {
      throw new PaymentGatewayException(e.getMessage());
    }
  }

  private static SessionCreateParams.LineItem getLineItem(OrderItem item) {
    return SessionCreateParams.LineItem.builder()
      .setQuantity((Long.valueOf(item.getQuantity())))
      .setPriceData(
        getPriceSession(item)
      )
      .build();
  }

  private static SessionCreateParams.LineItem.PriceData getPriceSession(OrderItem item) {
    return SessionCreateParams.LineItem.PriceData.builder()
      .setCurrency("usd")
      .setUnitAmountDecimal(item.getUnitPrice().multiply(new java.math.BigDecimal(100)))
      .setProductData(
        getProductSession(item)
      )
      .build();
  }

  private static SessionCreateParams.LineItem.PriceData.ProductData getProductSession(OrderItem item) {
    return SessionCreateParams.LineItem.PriceData.ProductData.builder()
      .setName(item.getProduct().getName())
      .build();
  }
}
