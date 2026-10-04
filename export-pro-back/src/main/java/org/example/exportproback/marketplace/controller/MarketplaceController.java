package org.example.exportproback.marketplace.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.exportproback.common.dto.ApiResponse;
import org.example.exportproback.marketplace.dto.*;
import org.example.exportproback.marketplace.service.MarketplaceService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/marketplace")
@RequiredArgsConstructor
public class MarketplaceController {

    private final MarketplaceService marketplaceService;

    @PostMapping("/listings")
    public ResponseEntity<ApiResponse<MarketplaceListingDto>> create(
            @Valid @RequestBody CreateListingRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ok(marketplaceService.create(request)));
    }

    @GetMapping("/listings")
    public ResponseEntity<ApiResponse<List<MarketplaceListingDto>>> getPublished() {
        return ResponseEntity.ok(ApiResponse.ok(marketplaceService.findPublished()));
    }

    @GetMapping("/listings/{id}")
    public ResponseEntity<ApiResponse<MarketplaceListingDto>> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(ApiResponse.ok(marketplaceService.findById(id)));
    }

    @GetMapping("/listings/company/{companyId}")
    public ResponseEntity<ApiResponse<List<MarketplaceListingDto>>> getByCompany(
            @PathVariable UUID companyId) {
        return ResponseEntity.ok(ApiResponse.ok(marketplaceService.findByCompanyId(companyId)));
    }

    @PostMapping("/listings/{id}/publish")
    public ResponseEntity<ApiResponse<MarketplaceListingDto>> publish(@PathVariable UUID id) {
        return ResponseEntity.ok(ApiResponse.ok(marketplaceService.publish(id)));
    }

    @PutMapping("/listings/{id}")
    public ResponseEntity<ApiResponse<MarketplaceListingDto>> update(
            @PathVariable UUID id,
            @Valid @RequestBody CreateListingRequest request) {
        return ResponseEntity.ok(ApiResponse.ok(marketplaceService.update(id, request)));
    }

    @PostMapping("/listings/{id}/inquiries")
    public ResponseEntity<ApiResponse<ContactInquiryDto>> sendInquiry(
            @PathVariable UUID id,
            @Valid @RequestBody SendInquiryRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ok(marketplaceService.sendInquiry(id, request)));
    }

    @GetMapping("/listings/{id}/inquiries")
    public ResponseEntity<ApiResponse<List<ContactInquiryDto>>> getInquiries(@PathVariable UUID id) {
        return ResponseEntity.ok(ApiResponse.ok(marketplaceService.getInquiries(id)));
    }
}
