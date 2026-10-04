package org.example.exportproback.questionnaire.repository;

import org.example.exportproback.product.domain.ProductCategory;
import org.example.exportproback.questionnaire.domain.Questionnaire;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface QuestionnaireRepository extends JpaRepository<Questionnaire, UUID> {
    List<Questionnaire> findByProductCategory(ProductCategory category);
    Optional<Questionnaire> findByProductCategoryAndActiveTrue(ProductCategory category);
}
