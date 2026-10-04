package org.example.exportproback.company.domain;

import jakarta.persistence.*;
import lombok.*;
import org.example.exportproback.auth.domain.User;
import org.example.exportproback.common.domain.BaseEntity;

import java.util.UUID;

@Entity
@Table(name = "companies")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Company extends BaseEntity {

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, length = 3)
    private String country;

    private String registrationNumber;

    private String vatNumber;

    private String address;

    private String website;

    private String phoneNumber;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CompanyStatus status;

    @Column(nullable = false)
    private UUID ownerId;
}
