package org.example.exportproback.questionnaire.repository;

import org.example.exportproback.questionnaire.domain.QuestionnaireResponse;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface QuestionnaireResponseRepository extends JpaRepository<QuestionnaireResponse, UUID> {
    List<QuestionnaireResponse> findByComplianceCaseId(UUID complianceCaseId);
    Optional<QuestionnaireResponse> findByComplianceCaseIdAndQuestionnaireId(UUID complianceCaseId, UUID questionnaireId);
}
