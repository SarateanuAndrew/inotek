package org.example.exportproback.marketplace.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class SendInquiryRequest {
    @NotBlank private String buyerName;
    @Email @NotBlank private String buyerEmail;
    private String buyerCompany;
    @NotBlank private String message;
}
