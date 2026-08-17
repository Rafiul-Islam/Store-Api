package com.storeapi.carts;

public class CartEmptyException extends RuntimeException {

  private static final String DEFAULT_MESSAGE = "Cart is empty.";

  public CartEmptyException() {
    this(DEFAULT_MESSAGE);
  }

  public CartEmptyException(String message) {
    super(message);
  }
}
