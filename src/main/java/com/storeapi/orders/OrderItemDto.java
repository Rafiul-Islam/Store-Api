package com.storeapi.orders;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class OrderItemDto {
  private orderProductDto product;
  private int quantity;
  private BigDecimal totalPrice;
}
