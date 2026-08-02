package com.storeapi.entities;

import com.storeapi.enums.OrderStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "orders")
public class Order {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private Long id;

  @ManyToOne
  @JoinColumn(name = "customer_id")
  private User customer;

  @Column(name = "status")
  @Enumerated(EnumType.STRING)
  private OrderStatus status;

  @Column(name = "total_price")
  private BigDecimal totalPrice;

  @Column(name = "created_at", insertable = false, updatable = false)
  private LocalDateTime createdAt;

  @OneToMany(mappedBy = "order", cascade = CascadeType.PERSIST)
  private Set<OrderItem> items = new HashSet<>();

  public void addOrderItem(OrderItem orderItem) {
    items.add(orderItem);
    orderItem.setOrder(this);
  }

}