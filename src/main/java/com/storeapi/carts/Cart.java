package com.storeapi.carts;

import com.storeapi.products.Product;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "carts")
public class Cart {
  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  @Column(name = "id")
  private UUID id;

  @Column(name = "date_created", insertable = false, updatable = false)
  private LocalDate dateCreated;

  @OneToMany(mappedBy = "cart", cascade = {CascadeType.MERGE, CascadeType.REMOVE}, fetch = FetchType.EAGER, orphanRemoval = true)
  private Set<CartItem> cartItems = new HashSet<>();

  public BigDecimal getTotal() {
    return cartItems.stream()
      .map(CartItem::getTotalPrice)
      .reduce(BigDecimal.ZERO, BigDecimal::add);
  }

  public CartItem getCartItem(Long productId) {
    return cartItems.stream()
      .filter(item -> item.getProduct().getId().equals(productId))
      .findFirst()
      .orElse(null);
  }

  public CartItem addCartItem(Product product) {
    CartItem cartItem = getCartItem(product.getId());

    if (cartItem != null) {
      cartItem.setQuantity(cartItem.getQuantity() + 1);
    } else {
      cartItem = new CartItem();
      cartItem.setProduct(product);
      cartItem.setQuantity(1);
      cartItem.addToCart(this);
    }
    cartItems.add(cartItem);

    return cartItem;
  }

  public void removeItem(Long productId) {
    CartItem cartItem = getCartItem(productId);
    cartItems.remove(cartItem);
    cartItem.setCart(null);
  }

  public void clearCart() {
    cartItems.clear();
  }

  public boolean isEmpty() {
    return cartItems.isEmpty();
  }
}