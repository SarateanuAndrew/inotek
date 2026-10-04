package org.example.exportproback.product.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.exportproback.common.dto.ApiResponse;
import org.example.exportproback.product.dto.CreateProductRequest;
import org.example.exportproback.product.dto.ProductDto;
import org.example.exportproback.product.service.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @PostMapping
    public ResponseEntity<ApiResponse<ProductDto>> create(@Valid @RequestBody CreateProductRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ok(productService.create(request)));
    }

    @GetMapping("/company/{companyId}")
    public ResponseEntity<ApiResponse<List<ProductDto>>> getByCompany(@PathVariable UUID companyId) {
        return ResponseEntity.ok(ApiResponse.ok(productService.findByCompanyId(companyId)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ProductDto>> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(ApiResponse.ok(productService.findById(id)));
    }
}
