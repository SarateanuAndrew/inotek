package org.example.exportproback.product.domain;

import jakarta.persistence.*;
import lombok.*;
import org.example.exportproback.common.domain.BaseEntity;

import java.util.UUID;

@Entity
@Table(name = "products")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Product extends BaseEntity {

    @Column(nullable = false)
    private UUID companyId;

    @Column(nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ProductCategory category;

    @Column(length = 2000)
    private String description;

    private String hsCode;

    private String ingredients;

    private String packagingType;

    private Integer weightGrams;

    private boolean organic;
}
