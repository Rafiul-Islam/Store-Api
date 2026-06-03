package com.storeapi.mappers;

import com.storeapi.dtos.ProductDto;
import com.storeapi.entities.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProductMapper {
  @Mapping(target = "categoryId", source = "category.id")
  ProductDto toDto(Product product);
  List<ProductDto> toDtoList(List<Product> product);
  @Mapping(target = "category", ignore = true)
  Product toEntity(ProductDto productDto);
  @Mapping(target = "id", ignore = true)
  void updateEntity(ProductDto productDto, @MappingTarget Product product);
}
