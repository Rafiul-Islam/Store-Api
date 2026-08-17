package com.storeapi.carts;

public class CartNotFoundException extends RuntimeException {
  private static final String DEFAULT_MESSAGE = "Cart not found.";

  public CartNotFoundException() {
    this(DEFAULT_MESSAGE);
  }

  public CartNotFoundException(String message) {
    super(message);
  }
}
