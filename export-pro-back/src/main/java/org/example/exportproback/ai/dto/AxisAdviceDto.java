package org.example.exportproback.ai.dto;

import lombok.Data;

@Data
public class AxisAdviceDto {
    private String axis;
    private String label;
    private int score;
    private String advice;
}
