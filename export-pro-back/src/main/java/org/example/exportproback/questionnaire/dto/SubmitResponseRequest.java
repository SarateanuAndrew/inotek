package org.example.exportproback.questionnaire.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.UUID;

@Data
public class SubmitResponseRequest {
    @NotNull
    private UUID complianceCaseId;
    @NotNull
    private UUID questionnaireId;
    @NotBlank
    private String responsesJson;
}
