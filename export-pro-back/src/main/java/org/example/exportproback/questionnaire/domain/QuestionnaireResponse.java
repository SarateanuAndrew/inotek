package org.example.exportproback.questionnaire.domain;

import jakarta.persistence.*;
import lombok.*;
import org.example.exportproback.common.domain.BaseEntity;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "questionnaire_responses")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QuestionnaireResponse extends BaseEntity {

    @Column(nullable = false)
    private UUID complianceCaseId;

    @Column(nullable = false)
    private UUID questionnaireId;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String responsesJson;

    private LocalDateTime completedAt;
}
