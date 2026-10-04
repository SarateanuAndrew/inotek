package org.example.exportproback.questionnaire.service;

import lombok.RequiredArgsConstructor;
import org.example.exportproback.questionnaire.domain.Questionnaire;
import org.example.exportproback.questionnaire.domain.QuestionnaireResponse;
import org.example.exportproback.questionnaire.dto.QuestionnaireDto;
import org.example.exportproback.questionnaire.dto.SubmitResponseRequest;
import org.example.exportproback.questionnaire.repository.QuestionnaireRepository;
import org.example.exportproback.questionnaire.repository.QuestionnaireResponseRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class QuestionnaireService {

    private final QuestionnaireRepository questionnaireRepository;
    private final QuestionnaireResponseRepository responseRepository;

    public QuestionnaireDto findById(UUID id) {
        return questionnaireRepository.findById(id).map(this::toDto)
                .orElseThrow(() -> new RuntimeException("Questionnaire not found"));
    }

    public QuestionnaireResponse submitResponse(SubmitResponseRequest request) {
        QuestionnaireResponse response = QuestionnaireResponse.builder()
                .complianceCaseId(request.getComplianceCaseId())
                .questionnaireId(request.getQuestionnaireId())
                .responsesJson(request.getResponsesJson())
                .completedAt(LocalDateTime.now())
                .build();
        return responseRepository.save(response);
    }

    private QuestionnaireDto toDto(Questionnaire q) {
        QuestionnaireDto dto = new QuestionnaireDto();
        dto.setId(q.getId());
        dto.setName(q.getName());
        dto.setVersion(q.getVersion());
        dto.setProductCategory(q.getProductCategory());
        dto.setQuestionsJson(q.getQuestionsJson());
        dto.setActive(q.isActive());
        return dto;
    }
}
