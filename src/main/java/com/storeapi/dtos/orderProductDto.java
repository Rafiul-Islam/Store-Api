package com.storeapi.dtos;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class orderProductDto {
  private Long id;
  private String name;
  private BigDecimal price;
}
