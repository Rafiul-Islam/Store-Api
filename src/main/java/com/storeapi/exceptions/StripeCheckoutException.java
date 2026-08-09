package com.storeapi.exceptions;

import com.stripe.exception.StripeException;

public class StripeCheckoutException extends RuntimeException {
  private static final String DEFAULT_MESSAGE = "Error creating Stripe checkout session.";

  public StripeCheckoutException() {
    this(DEFAULT_MESSAGE);
  }

  public StripeCheckoutException(StripeException stripeException) {
    this(stripeException.getMessage());
  }

  public StripeCheckoutException(String message) {
    super(message);
  }
}
