package com.storeapi.products;

import com.storeapi.carts.ProductDto;
import com.storeapi.services.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;

@Tag(name = "Products", description = "All products related endpoints")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/products")
public class ProductController {

  private final ProductService productService;
  private final ProductMapper productMapper;

  @GetMapping
  @Operation(
    summary = "Get all products",
    description = "Fetch list of all products. Optionally filter by categoryId."
  )
  public ResponseEntity<List<ProductDto>> getAllProducts(
    @RequestParam(required = false, name = "categoryId") Byte categoryId
  ) {
    List<Product> products = productService.findAll(categoryId);
    return ResponseEntity.ok(productMapper.toDtoList(products));
  }

  @GetMapping("/{productId}")
  @Operation(
    summary = "Get product by ID",
    description = "Retrieve a single product by its unique ID."
  )
  public ResponseEntity<ProductDto> getProductById(@PathVariable Long productId) {
    Product product = productService.findById(productId);
    ProductDto productDto = productMapper.toDto(product);
    return ResponseEntity.ok(productDto);
  }

  @PostMapping
  @Operation(
    summary = "Create new product",
    description = "Create a new product and return the created resource with location header."
  )
  public ResponseEntity<ProductDto> createProduct(
    @RequestBody ProductDto productDto,
    UriComponentsBuilder uriComponentsBuilder
  ) {
    Product savedProduct = productService.save(productDto);
    ProductDto newProductDto = productMapper.toDto(savedProduct);

    URI uri = uriComponentsBuilder
      .path("/api/products/{id}")
      .buildAndExpand(newProductDto.getId())
      .toUri();

    return ResponseEntity.created(uri).body(newProductDto);
  }

  @PutMapping("/{productId}")
  @Operation(
    summary = "Update product",
    description = "Update an existing product by ID."
  )
  public ResponseEntity<ProductDto> updateProduct(
    @PathVariable(name = "productId") Long productId,
    @RequestBody ProductDto productDto
  ) {
    Product product = productService.update(productId, productDto);
    ProductDto updatedProductDto = productMapper.toDto(product);
    return ResponseEntity.ok(updatedProductDto);
  }

  @DeleteMapping("/{productId}")
  @Operation(
    summary = "Delete product",
    description = "Delete a product by its ID."
  )
  public ResponseEntity<ProductDto> deleteProduct(
    @PathVariable(name = "productId") Long productId
  ) {
    productService.delete(productId);
    return ResponseEntity.noContent().build();
  }
}