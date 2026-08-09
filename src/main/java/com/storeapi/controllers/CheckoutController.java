package com.storeapi.controllers;

import com.storeapi.dtos.CheckoutRequest;
import com.storeapi.dtos.CheckoutResponse;
import com.storeapi.services.CheckoutService;
import com.stripe.exception.StripeException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/checkout")
public class CheckoutController {
  private final CheckoutService checkoutService;

  @PostMapping
  public ResponseEntity<CheckoutResponse> checkout(
    @Valid @RequestBody CheckoutRequest checkoutRequest
  ) throws StripeException {
    CheckoutResponse checkoutResponse = checkoutService.checkout(checkoutRequest);
    return ResponseEntity.ok(checkoutResponse);
  }

}
