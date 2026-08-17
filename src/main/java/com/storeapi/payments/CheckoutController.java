package com.storeapi.payments;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/checkout")
public class CheckoutController {
  private final CheckoutService checkoutService;

  @PostMapping
  public ResponseEntity<CheckoutResponse> checkout(
    @Valid @RequestBody CheckoutRequest checkoutRequest
  ) {
    CheckoutResponse checkoutResponse = checkoutService.checkout(checkoutRequest);
    return ResponseEntity.ok(checkoutResponse);
  }

  @PostMapping("/webhook")
  public void handleWebhook(
    @RequestHeader Map<String, String> headers,
    @RequestBody String payload
  ) {
    checkoutService.handleWebHookEvent(new WebhookRequest(headers, payload));
  }

  @AllArgsConstructor
  @Data
  public static class PaymentResult {
    private long orderId;
    private PaymentStatus paymentStatus;
  }
}
