package com.storeapi.repositories;

import com.storeapi.entities.ActiveToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ActiveTokenRepository extends JpaRepository<ActiveToken, UUID> {
  boolean existsByTokenId(UUID token);
  void deleteByTokenId(UUID token);
}