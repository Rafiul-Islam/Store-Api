package com.storeapi.services;

import com.storeapi.entities.Product;
import com.storeapi.repositories.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class ProductService {
  private final ProductRepository productRepository;

  public List<Product> findAll(Byte categoryId) {
    if (categoryId == null) return productRepository.findAll();
    return productRepository.findByCategoryId(categoryId);
  }

  public Product findById(long id) {
    return productRepository.findById(id).orElseThrow(() -> new RuntimeException("Product not found"));
  }
}
