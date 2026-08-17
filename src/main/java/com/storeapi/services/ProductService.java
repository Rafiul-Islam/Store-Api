package com.storeapi.services;

import com.storeapi.carts.ProductDto;
import com.storeapi.products.Category;
import com.storeapi.products.Product;
import com.storeapi.products.ProductMapper;
import com.storeapi.repositories.CategoryRepository;
import com.storeapi.repositories.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class ProductService {
  private final ProductRepository productRepository;
  private final ProductMapper productMapper;
  private final CategoryRepository categoryRepository;

  public List<Product> findAll(Byte categoryId) {
    if (categoryId == null) return productRepository.findAll();
    return productRepository.findByCategoryId(categoryId);
  }

  public Product findById(long id) {
    return productRepository.findById(id).orElseThrow(() -> new RuntimeException("Product not found"));
  }

  public Optional<Product> getById(long id) {
    return productRepository.findById(id);
  }

  public Product save(ProductDto productDto) {
    Category existingCategory = categoryRepository.findById(productDto.getCategoryId()).orElseThrow(() -> new RuntimeException("Category not found"));
    Product product = productMapper.toEntity(productDto);
    product.setCategory(existingCategory);
    return productRepository.save(product);
  }

  public Product update(Long productId, ProductDto productDto) {
    Category existingCategory = categoryRepository.findById(productDto.getCategoryId()).orElseThrow(() -> new RuntimeException("Category not found"));
    Product existingProduct = productRepository.findById(productId).orElseThrow(() -> new RuntimeException("Product not found"));
    productMapper.updateEntity(productDto, existingProduct);
    existingProduct.setCategory(existingCategory);
    return productRepository.save(existingProduct);
  }

  public void delete(Long productId) {
    Product existingProduct = productRepository.findById(productId).orElseThrow(() -> new RuntimeException("Product not found"));
    productRepository.delete(existingProduct);
  }
}
