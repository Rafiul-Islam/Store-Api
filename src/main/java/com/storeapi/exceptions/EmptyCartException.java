package com.storeapi.exceptions;

public class EmptyCartException extends RuntimeException {

  private static final String DEFAULT_MESSAGE = "Cart is empty.";

  public EmptyCartException() {
    this(DEFAULT_MESSAGE);
  }

  public EmptyCartException(String message) {
    super(message);
  }
}
