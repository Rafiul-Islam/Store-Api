package com.storeapi.services;

import com.storeapi.entities.Order;
import com.storeapi.repositories.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class OrderService {
  private final OrderRepository orderRepository;

  public List<Order> findAll() {
    return orderRepository.findAll();
  }

  public Order findById(long orderId) {
    return orderRepository.findOrderWithItemsByOrderId(orderId).orElseThrow(() -> new RuntimeException("Order not found"));
  }
}
