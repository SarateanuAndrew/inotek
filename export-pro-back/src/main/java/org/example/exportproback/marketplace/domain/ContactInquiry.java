package org.example.exportproback.marketplace.domain;

import jakarta.persistence.*;
import lombok.*;
import org.example.exportproback.common.domain.BaseEntity;

import java.util.UUID;

@Entity
@Table(name = "contact_inquiries")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ContactInquiry extends BaseEntity {

    @Column(nullable = false)
    private UUID listingId;

    @Column(nullable = false)
    private String buyerName;

    @Column(nullable = false)
    private String buyerEmail;

    private String buyerCompany;

    @Column(nullable = false, length = 2000)
    private String message;

    @Column(nullable = false)
    private boolean read;
}
