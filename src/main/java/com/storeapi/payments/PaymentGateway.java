package com.storeapi.payments;

import com.storeapi.orders.Order;

import java.util.Optional;

public interface PaymentGateway {
  CheckoutSession createCheckoutSession(Order order);
  Optional<CheckoutController.PaymentResult> parseWebhookRequest(WebhookRequest webhookRequest);
}
