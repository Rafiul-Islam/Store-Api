package com.storeapi.controllers;

import com.storeapi.dtos.ProductDto;
import com.storeapi.entities.Product;
import com.storeapi.mappers.ProductMapper;
import com.storeapi.services.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/products")
public class ProductController {
  private final ProductService productService;
  private final ProductMapper productMapper;

  @GetMapping
  public ResponseEntity<List<ProductDto>> findAll(
    @RequestParam(required = false, name = "categoryId") Byte categoryId
  ) {
    List<Product> products = productService.getProducts(categoryId);
    return ResponseEntity.ok(productMapper.toDtoList(products));
  }

  @GetMapping("/{productId}")
  public ResponseEntity<ProductDto> findById(@PathVariable Long productId) {
    Optional<Product> productOpt = productService.getProductById(productId);
    if (productOpt.isPresent()) {
      return ResponseEntity.ok(productMapper.toDto(productOpt.get()));
    }
    return ResponseEntity.notFound().build();
  }
}
