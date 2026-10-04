package org.example.exportproback.roadmap.domain;

import jakarta.persistence.*;
import lombok.*;
import org.example.exportproback.common.domain.BaseEntity;

import java.util.UUID;

@Entity
@Table(name = "company_roadmaps")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CompanyRoadmap extends BaseEntity {

    @Column(nullable = false)
    private UUID companyId;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String scoresJson;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String adviceJson;
}
