package com.storeapi.carts;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CartRepository extends JpaRepository<Cart, UUID> {
  @EntityGraph(attributePaths = {"cartItems.product"})
  @Query("select c from Cart c where c.id = :cartId")
  Optional<Cart> findCartWithItemsByCartId(@Param("cartId") UUID cartId);
  @EntityGraph(attributePaths = {"cartItems", "cartItems.product"})
  @Query("select c from Cart c")
  List<Cart> findAllWithItems();
}