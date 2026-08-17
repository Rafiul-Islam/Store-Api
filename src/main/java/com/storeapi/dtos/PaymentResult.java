package com.storeapi.dtos;

import com.storeapi.enums.PaymentStatus;
import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class PaymentResult {
  private long orderId;
  private PaymentStatus paymentStatus;
}
