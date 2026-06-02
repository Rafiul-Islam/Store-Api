package com.storeapi.services;

import com.storeapi.entities.Product;
import com.storeapi.repositories.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class ProductService {
  private final ProductRepository productRepository;

  public List<Product> getProducts(Byte categoryId) {
    if (categoryId == null) return productRepository.findAll();
    return productRepository.findByCategoryId(categoryId);
  }

  public Optional<Product> getProductById(long id) {
    return productRepository.findById(id);
  }
}
