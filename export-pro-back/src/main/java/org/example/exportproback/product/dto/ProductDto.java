package org.example.exportproback.product.dto;

import lombok.Data;
import org.example.exportproback.product.domain.ProductCategory;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class ProductDto {
    private UUID id;
    private UUID companyId;
    private String name;
    private ProductCategory category;
    private String description;
    private String hsCode;
    private String ingredients;
    private String packagingType;
    private Integer weightGrams;
    private boolean organic;
    private LocalDateTime createdAt;
}
