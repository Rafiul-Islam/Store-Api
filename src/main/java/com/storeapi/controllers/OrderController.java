package com.storeapi.controllers;

import com.storeapi.dtos.OrderDto;
import com.storeapi.entities.Order;
import com.storeapi.mappers.OrderMapper;
import com.storeapi.services.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/orders")
public class OrderController {
  private final OrderService orderService;
  private final OrderMapper orderMapper;

  @GetMapping
  public ResponseEntity<List<OrderDto>> getAllOrders() {
    List<Order> orders = orderService.findAll();
    List<OrderDto> orderDtos = orderMapper.toDtoList(orders);
    return ResponseEntity.ok(orderDtos);
  }

  @GetMapping("{orderId}")
  @Operation(
    summary = "Get order by ID",
    description = "Retrieve a order along with its items using order id."
  )
  public ResponseEntity<OrderDto> getOrder(
    @PathVariable(name = "orderId") long orderId
  ) {
    Order order = orderService.findById(orderId);
    OrderDto orderDto = orderMapper.toDto(order);
    return ResponseEntity.ok(orderDto);
  }

}
