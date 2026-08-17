package com.storeapi.services;

import com.storeapi.dtos.CheckoutSession;
import com.storeapi.dtos.PaymentResult;
import com.storeapi.dtos.WebhookRequest;
import com.storeapi.entities.Order;

import java.util.Optional;

public interface PaymentGateway {
  CheckoutSession createCheckoutSession(Order order);
  Optional<PaymentResult> parseWebhookRequest(WebhookRequest webhookRequest);
}
