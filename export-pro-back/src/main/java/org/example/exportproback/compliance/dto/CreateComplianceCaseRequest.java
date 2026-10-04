package org.example.exportproback.compliance.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.UUID;

@Data
public class CreateComplianceCaseRequest {
    @NotNull
    private UUID productId;
    @NotNull
    private UUID companyId;
    @NotBlank
    private String originCountry;
    @NotBlank
    private String targetCountry;
}
