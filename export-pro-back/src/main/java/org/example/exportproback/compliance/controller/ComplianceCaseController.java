package org.example.exportproback.compliance.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.exportproback.common.dto.ApiResponse;
import org.example.exportproback.compliance.domain.ComplianceCaseStatus;
import org.example.exportproback.compliance.dto.*;
import org.example.exportproback.compliance.service.ComplianceCaseService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/compliance-cases")
@RequiredArgsConstructor
public class ComplianceCaseController {

    private final ComplianceCaseService complianceCaseService;

    @PostMapping
    public ResponseEntity<ApiResponse<ComplianceCaseDto>> create(
            @Valid @RequestBody CreateComplianceCaseRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ok(complianceCaseService.create(request)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ComplianceCaseDto>> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(ApiResponse.ok(complianceCaseService.findById(id)));
    }

    @GetMapping("/company/{companyId}")
    public ResponseEntity<ApiResponse<List<ComplianceCaseDto>>> getByCompany(@PathVariable UUID companyId) {
        return ResponseEntity.ok(ApiResponse.ok(complianceCaseService.findByCompanyId(companyId)));
    }

    @GetMapping("/{id}/requirements")
    public ResponseEntity<ApiResponse<List<RequirementDto>>> getRequirements(@PathVariable UUID id) {
        return ResponseEntity.ok(ApiResponse.ok(complianceCaseService.getRequirements(id)));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<ApiResponse<ComplianceCaseDto>> updateStatus(
            @PathVariable UUID id, @RequestParam ComplianceCaseStatus status) {
        return ResponseEntity.ok(ApiResponse.ok(complianceCaseService.updateStatus(id, status)));
    }
}
