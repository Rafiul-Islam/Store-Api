package com.storeapi.orders;

import com.storeapi.carts.Cart;
import com.storeapi.users.User;
import com.storeapi.payments.PaymentStatus;
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
  private PaymentStatus status;

  @Column(name = "total_price")
  private BigDecimal totalPrice;

  @Column(name = "created_at", insertable = false, updatable = false)
  private LocalDateTime createdAt;

  @OneToMany(mappedBy = "order", cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
  private Set<OrderItem> items = new HashSet<>();

  public static Order frommCart(Cart cart, User customer) {
    Order order = new Order();
    order.setCustomer(customer);
    order.setStatus(PaymentStatus.PENDING);
    order.setTotalPrice(cart.getTotal());

    cart.getCartItems().forEach(cartItem -> {
      OrderItem orderItem = new OrderItem(order, cartItem.getProduct(), cartItem.getQuantity());
      order.items.add(orderItem);
    });

    return order;
  }

}