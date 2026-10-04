package org.example.exportproback.distributor.domain;

import jakarta.persistence.*;
import lombok.*;
import org.example.exportproback.common.domain.BaseEntity;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "distributors")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Distributor extends BaseEntity {

    @Column(nullable = false)
    private UUID userId;

    @Column(nullable = false)
    private String companyName;

    @Column(nullable = false, unique = true)
    private String cui;

    private String vatNumber;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DistributorStatus status;

    private String rejectionReason;

    private LocalDateTime verifiedAt;
}
