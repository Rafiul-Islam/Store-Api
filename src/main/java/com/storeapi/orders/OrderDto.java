package com.storeapi.orders;

import com.storeapi.payments.PaymentStatus;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class OrderDto {
  private long id;
  private PaymentStatus status;
  private LocalDateTime createdAt;
  private List<OrderItemDto> items;
  private BigDecimal totalPrice;
}
