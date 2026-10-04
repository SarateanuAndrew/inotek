package org.example.exportproback.distributor.dto;

import lombok.Data;
import org.example.exportproback.distributor.domain.DistributorStatus;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class DistributorDto {
    private UUID id;
    private UUID userId;
    private String companyName;
    private String cui;
    private String vatNumber;
    private DistributorStatus status;
    private String rejectionReason;
    private LocalDateTime verifiedAt;
    private LocalDateTime createdAt;
}
