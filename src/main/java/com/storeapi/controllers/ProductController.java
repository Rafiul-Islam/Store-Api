package com.storeapi.controllers;

import com.storeapi.dtos.ProductDto;
import com.storeapi.entities.Product;
import com.storeapi.mappers.ProductMapper;
import com.storeapi.services.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/products")
public class ProductController {
  private final ProductService productService;
  private final ProductMapper productMapper;

  @GetMapping
  public ResponseEntity<List<ProductDto>> getAllProducts(
    @RequestParam(required = false, name = "categoryId") Byte categoryId
  ) {
    List<Product> products = productService.findAll(categoryId);
    return ResponseEntity.ok(productMapper.toDtoList(products));
  }

  @GetMapping("/{productId}")
  public ResponseEntity<ProductDto> getProductById(@PathVariable Long productId) {
    Product product = productService.findById(productId);
    ProductDto productDto = productMapper.toDto(product);
    return ResponseEntity.ok(productDto);
  }
}
