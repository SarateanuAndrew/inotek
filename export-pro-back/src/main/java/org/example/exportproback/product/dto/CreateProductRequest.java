package org.example.exportproback.product.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.example.exportproback.product.domain.ProductCategory;

import java.util.UUID;

@Data
public class CreateProductRequest {
    @NotNull
    private UUID companyId;
    @NotBlank
    private String name;
    @NotNull
    private ProductCategory category;
    private String description;
    private String ingredients;
    private String packagingType;
    private Integer weightGrams;
    private boolean organic;
}
