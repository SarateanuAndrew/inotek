package org.example.exportproback.company.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.exportproback.auth.domain.User;
import org.example.exportproback.common.dto.ApiResponse;
import org.example.exportproback.company.dto.CompanyDto;
import org.example.exportproback.company.dto.CreateCompanyRequest;
import org.example.exportproback.company.service.CompanyService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/companies")
@RequiredArgsConstructor
public class CompanyController {

    private final CompanyService companyService;

    @PostMapping
    @PreAuthorize("hasRole('PRODUCER')")
    public ResponseEntity<ApiResponse<CompanyDto>> create(
            @Valid @RequestBody CreateCompanyRequest request,
            @AuthenticationPrincipal User currentUser) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ok(companyService.create(request, currentUser)));
    }

    @GetMapping
    @PreAuthorize("hasRole('PRODUCER')")
    public ResponseEntity<ApiResponse<List<CompanyDto>>> getMyCompanies(
            @AuthenticationPrincipal User currentUser) {
        return ResponseEntity.ok(ApiResponse.ok(companyService.findByCurrentUser(currentUser)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<CompanyDto>> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(ApiResponse.ok(companyService.findById(id)));
    }
}
