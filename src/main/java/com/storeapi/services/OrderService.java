package com.storeapi.services;

import com.storeapi.entities.Order;
import com.storeapi.repositories.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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

  public Optional<Order> getById(long orderId) {
    return orderRepository.findById(orderId);
  }

  public Order save(Order order) {
    return orderRepository.save(order);
  }
}
