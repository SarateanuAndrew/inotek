package org.example.exportproback.product.service;

import lombok.RequiredArgsConstructor;
import org.example.exportproback.common.exception.ResourceNotFoundException;
import org.example.exportproback.product.domain.Product;
import org.example.exportproback.product.dto.CreateProductRequest;
import org.example.exportproback.product.dto.ProductDto;
import org.example.exportproback.product.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    public ProductDto create(CreateProductRequest request) {
        Product product = Product.builder()
                .companyId(request.getCompanyId())
                .name(request.getName())
                .category(request.getCategory())
                .description(request.getDescription())
                .ingredients(request.getIngredients())
                .packagingType(request.getPackagingType())
                .weightGrams(request.getWeightGrams())
                .organic(request.isOrganic())
                .build();
        return toDto(productRepository.save(product));
    }

    public List<ProductDto> findByCompanyId(UUID companyId) {
        return productRepository.findByCompanyId(companyId).stream().map(this::toDto).toList();
    }

    public ProductDto findById(UUID id) {
        return toDto(productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product", id)));
    }

    private ProductDto toDto(Product p) {
        ProductDto dto = new ProductDto();
        dto.setId(p.getId());
        dto.setCompanyId(p.getCompanyId());
        dto.setName(p.getName());
        dto.setCategory(p.getCategory());
        dto.setDescription(p.getDescription());
        dto.setHsCode(p.getHsCode());
        dto.setIngredients(p.getIngredients());
        dto.setPackagingType(p.getPackagingType());
        dto.setWeightGrams(p.getWeightGrams());
        dto.setOrganic(p.isOrganic());
        dto.setCreatedAt(p.getCreatedAt());
        return dto;
    }
}
