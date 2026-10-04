package org.example.exportproback.questionnaire.dto;

import lombok.Data;
import org.example.exportproback.product.domain.ProductCategory;

import java.util.UUID;

@Data
public class QuestionnaireDto {
    private UUID id;
    private String name;
    private String version;
    private ProductCategory productCategory;
    private String questionsJson;
    private boolean active;
}
