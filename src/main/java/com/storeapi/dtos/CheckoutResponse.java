package com.storeapi.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class CheckoutResponse {
  private long orderId;
  private String checkoutUrl;
}
