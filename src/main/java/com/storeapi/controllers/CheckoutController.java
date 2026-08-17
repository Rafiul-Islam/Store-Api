package com.storeapi.controllers;

import com.storeapi.dtos.CheckoutRequest;
import com.storeapi.dtos.CheckoutResponse;
import com.storeapi.dtos.WebhookRequest;
import com.storeapi.services.CheckoutService;
import jakarta.validation.Valid;
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

}
