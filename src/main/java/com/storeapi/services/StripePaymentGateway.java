package com.storeapi.services;

import com.storeapi.dtos.CheckoutSession;
import com.storeapi.dtos.PaymentResult;
import com.storeapi.dtos.WebhookRequest;
import com.storeapi.entities.Order;
import com.storeapi.entities.OrderItem;
import com.storeapi.enums.PaymentStatus;
import com.storeapi.exceptions.PaymentGatewayException;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.exception.StripeException;
import com.stripe.model.Event;
import com.stripe.model.PaymentIntent;
import com.stripe.model.StripeObject;
import com.stripe.model.checkout.Session;
import com.stripe.net.Webhook;
import com.stripe.param.checkout.SessionCreateParams;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class StripePaymentGateway implements PaymentGateway {

  @Value("${stripe.webhookSecret}")
  private String stripeWebhookSecret;

  @Value("${website.url}")
  private String websiteUrl;

  @Override
  public CheckoutSession createCheckoutSession(Order order) {
    try {
      SessionCreateParams.Builder stripeSessionBuilder = SessionCreateParams.builder()
        .setMode(SessionCreateParams.Mode.PAYMENT)
        .setSuccessUrl(websiteUrl + "/checkout-success?order" + order.getId())
        .setCancelUrl(websiteUrl + "/checkout-cancel")
        .putMetadata("orderId", order.getId().toString());

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

  @Override
  public Optional<PaymentResult> parseWebhookRequest(WebhookRequest webhookRequest) {
    try {
      String payload = webhookRequest.getPayload();
      String signature = webhookRequest.getHeaders().get("Stripe-Signature");
      Event event = Webhook.constructEvent(payload, signature, stripeWebhookSecret);

      long orderId = extractOrderId(event);

      return switch (event.getType()) {
        case "payment_intent.succeeded" -> Optional.of(new PaymentResult(orderId, PaymentStatus.PAID));
        case "payment_intent.payment_failed" -> Optional.of(new PaymentResult(orderId, PaymentStatus.FAILED));
        default -> Optional.empty();
      };
    } catch (SignatureVerificationException e) {
      throw new PaymentGatewayException("Invalid Stripe Signature");
    }
  }

  private long extractOrderId(Event event) {
    StripeObject stripeObject = event.getDataObjectDeserializer().getObject()
      .orElseThrow(() -> new PaymentGatewayException("Could not deserialize stripe event. Please check the SDK or API version."));
    var paymentIntent = (PaymentIntent) stripeObject;
    String orderId = paymentIntent.getMetadata().get("orderId");
    return Long.parseLong(orderId);
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
