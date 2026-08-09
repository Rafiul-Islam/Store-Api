package com.storeapi.services;

import com.storeapi.dtos.CheckoutSession;
import com.storeapi.entities.Order;

public interface PaymentGateway {
  public CheckoutSession createCheckoutSession(Order order);
}
