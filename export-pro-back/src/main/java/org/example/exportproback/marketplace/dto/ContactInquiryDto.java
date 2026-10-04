package org.example.exportproback.marketplace.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class ContactInquiryDto {
    private UUID id;
    private UUID listingId;
    private String buyerName;
    private String buyerEmail;
    private String buyerCompany;
    private String message;
    private boolean read;
    private LocalDateTime createdAt;
}
