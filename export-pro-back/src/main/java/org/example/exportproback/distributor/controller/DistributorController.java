package org.example.exportproback.distributor.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.exportproback.auth.domain.User;
import org.example.exportproback.common.dto.ApiResponse;
import org.example.exportproback.distributor.dto.DistributorDto;
import org.example.exportproback.distributor.dto.RegisterDistributorRequest;
import org.example.exportproback.distributor.service.DistributorService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/distributors")
@RequiredArgsConstructor
public class DistributorController {

    private final DistributorService distributorService;

    @GetMapping("/me")
    @PreAuthorize("hasRole('DISTRIBUTOR')")
    public ResponseEntity<ApiResponse<DistributorDto>> getMe(@AuthenticationPrincipal User currentUser) {
        return distributorService.findByUserId(currentUser.getId())
                .map(d -> ResponseEntity.ok(ApiResponse.ok(d)))
                .orElse(ResponseEntity.ok(ApiResponse.ok(null)));
    }

    @PostMapping("/register")
    @PreAuthorize("hasRole('DISTRIBUTOR')")
    public ResponseEntity<ApiResponse<DistributorDto>> register(
            @Valid @RequestBody RegisterDistributorRequest request,
            @AuthenticationPrincipal User currentUser) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ok(distributorService.register(request, currentUser)));
    }

    @PostMapping("/{id}/verify")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<DistributorDto>> verify(@PathVariable UUID id) {
        return ResponseEntity.ok(ApiResponse.ok(distributorService.verify(id)));
    }

    @PostMapping("/{id}/reject")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<DistributorDto>> reject(
            @PathVariable UUID id, @RequestParam String reason) {
        return ResponseEntity.ok(ApiResponse.ok(distributorService.reject(id, reason)));
    }
}
