package com.storeapi.controllers;

import com.storeapi.dtos.CheckoutRequest;
import com.storeapi.dtos.CheckoutResponse;
import com.storeapi.enums.OrderStatus;
import com.storeapi.services.CheckoutService;
import com.storeapi.services.OrderService;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.model.Event;
import com.stripe.model.PaymentIntent;
import com.stripe.model.StripeObject;
import com.stripe.net.Webhook;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/checkout")
public class CheckoutController {
  private final CheckoutService checkoutService;
  private final OrderService orderService;

  @Value("${stripe.webhookSecret}")
  private String stripeWebhookSecret;

  @PostMapping
  public ResponseEntity<CheckoutResponse> checkout(
    @Valid @RequestBody CheckoutRequest checkoutRequest
  ) {
    CheckoutResponse checkoutResponse = checkoutService.checkout(checkoutRequest);
    return ResponseEntity.ok(checkoutResponse);
  }

  @PostMapping("/webhook")
  public ResponseEntity<Void> handleWebhook(
    @RequestHeader("Stripe-Signature") String signature,
    @RequestBody String payload
  ) {
    try {
      Event event = Webhook.constructEvent(payload, signature, stripeWebhookSecret);
      System.out.println("Received Stripe event: " + event.getType());

      StripeObject stripeObject = event.getDataObjectDeserializer().getObject().orElse(null);

      switch (event.getType()) {
        case "payment_intent.succeeded" -> {
          // TODO: update order status
          var paymentIntent = (PaymentIntent) stripeObject;
          if (paymentIntent != null) {
            String orderId = paymentIntent.getMetadata().get("orderId");
            orderService.getById(Long.parseLong(orderId)).ifPresent(order -> {
              order.setStatus(OrderStatus.PAID);
              orderService.save(order);
            });
          }
        }
        case "payment_intent.failed" -> {
          // TODO: update order status
          var paymentIntent = (PaymentIntent) stripeObject;
          if (paymentIntent != null) {
            String orderId = paymentIntent.getMetadata().get("orderId");
            orderService.getById(Long.parseLong(orderId)).ifPresent(order -> {
              order.setStatus(OrderStatus.FAILED);
              orderService.save(order);
            });
          }
        }
      }

      return ResponseEntity.ok().build();
    } catch (SignatureVerificationException e) {
      throw new RuntimeException(e);
    }
  }

}
