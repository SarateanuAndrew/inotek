package org.example.exportproback.company.dto;

import lombok.Data;
import org.example.exportproback.company.domain.CompanyStatus;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class CompanyDto {
    private UUID id;
    private String name;
    private String country;
    private String registrationNumber;
    private String vatNumber;
    private String address;
    private String website;
    private String phoneNumber;
    private CompanyStatus status;
    private UUID ownerId;
    private LocalDateTime createdAt;
}
