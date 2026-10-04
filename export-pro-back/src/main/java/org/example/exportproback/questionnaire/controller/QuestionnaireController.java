package org.example.exportproback.questionnaire.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.exportproback.common.dto.ApiResponse;
import org.example.exportproback.questionnaire.dto.QuestionnaireDto;
import org.example.exportproback.questionnaire.dto.SubmitResponseRequest;
import org.example.exportproback.questionnaire.service.QuestionnaireService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/questionnaires")
@RequiredArgsConstructor
public class QuestionnaireController {

    private final QuestionnaireService questionnaireService;

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<QuestionnaireDto>> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(ApiResponse.ok(questionnaireService.findById(id)));
    }

    @PostMapping("/responses")
    public ResponseEntity<ApiResponse<String>> submitResponse(@Valid @RequestBody SubmitResponseRequest request) {
        questionnaireService.submitResponse(request);
        return ResponseEntity.ok(ApiResponse.ok("Response submitted successfully", null));
    }
}
