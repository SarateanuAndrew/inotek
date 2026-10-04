package org.example.exportproback.company.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CreateCompanyRequest {
    @NotBlank
    private String name;
    @NotBlank @Size(min = 2, max = 3)
    private String country;
    private String registrationNumber;
    private String vatNumber;
    private String address;
    private String website;
    private String phoneNumber;
}
