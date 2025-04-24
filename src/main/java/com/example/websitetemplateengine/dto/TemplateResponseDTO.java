package com.example.websitetemplateengine.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@Schema(description = "Response (template .html file)")
public class TemplateResponseDTO {
    private String template;
}
