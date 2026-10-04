package org.example.exportproback.questionnaire.domain;

import jakarta.persistence.*;
import lombok.*;
import org.example.exportproback.common.domain.BaseEntity;
import org.example.exportproback.product.domain.ProductCategory;

@Entity
@Table(name = "questionnaires")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Questionnaire extends BaseEntity {

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String version;

    @Enumerated(EnumType.STRING)
    private ProductCategory productCategory;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String questionsJson;

    private boolean active = true;
}
