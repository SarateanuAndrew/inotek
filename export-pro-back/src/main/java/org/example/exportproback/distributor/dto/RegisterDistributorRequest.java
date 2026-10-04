package org.example.exportproback.distributor.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class RegisterDistributorRequest {
    @NotBlank private String companyName;
    @NotBlank private String cui;
    private String vatNumber;
}
