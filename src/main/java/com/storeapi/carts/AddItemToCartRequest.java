package com.storeapi.carts;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;

@Data
public class AddItemToCartRequest {
  @NotNull(message = "Product Id is required")
  @PositiveOrZero(message = "Product Id must be a positive number")
  private Long productId;
}
