package com.storeapi.controllers;

import com.storeapi.dtos.ProductDto;
import com.storeapi.entities.Product;
import com.storeapi.mappers.ProductMapper;
import com.storeapi.services.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/products" )
public class ProductController {
  private final ProductService productService;
  private final ProductMapper productMapper;

  @GetMapping
  public ResponseEntity<List<ProductDto>> getAllProducts(
    @RequestParam(required = false, name = "categoryId" ) Byte categoryId
  ) {
    List<Product> products = productService.findAll(categoryId);
    return ResponseEntity.ok(productMapper.toDtoList(products));
  }

  @GetMapping("/{productId}" )
  public ResponseEntity<ProductDto> getProductById(@PathVariable Long productId) {
    Product product = productService.findById(productId);
    ProductDto productDto = productMapper.toDto(product);
    return ResponseEntity.ok(productDto);
  }

  @PostMapping
  public ResponseEntity<ProductDto> createProduct(
    @RequestBody ProductDto productDto,
    UriComponentsBuilder uriComponentsBuilder
  ) {
    Product savedProduct = productService.save(productDto);
    ProductDto newProductDto = productMapper.toDto(savedProduct);
    URI uri = uriComponentsBuilder.path("/api/products/{id}" ).buildAndExpand(newProductDto.getId()).toUri();
    return ResponseEntity.created(uri).body(newProductDto);
  }

  @PutMapping("/{productId}" )
  public ResponseEntity<ProductDto> updateProduct(
    @PathVariable(name = "productId" ) Long productId,
    @RequestBody ProductDto productDto
  ) {
    Product product = productService.update(productId, productDto);
    ProductDto updatedProductDto = productMapper.toDto(product);
    return ResponseEntity.ok(updatedProductDto);
  }

  @DeleteMapping("/{productId}" )
  public ResponseEntity<ProductDto> deleteProduct(
    @PathVariable(name = "productId" ) Long productId
  ) {
    productService.delete(productId);
    return ResponseEntity.noContent().build();
  }
}
