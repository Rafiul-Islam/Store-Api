package com.storeapi.exceptions;

public class PaymentGatewayException extends RuntimeException {
  private static final String DEFAULT_MESSAGE = "Error creating payment checkout session.";

  public PaymentGatewayException() {
    this(DEFAULT_MESSAGE);
  }

  public PaymentGatewayException(String message) {
    super(message);
  }
}
