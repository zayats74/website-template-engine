package com.example.websitetemplateengine.controller;

import com.example.websitetemplateengine.dto.TemplateRequestDTO;
import com.example.websitetemplateengine.dto.TemplateResponseDTO;
import com.example.websitetemplateengine.service.TemplateService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("api/v1/templates")
@RequiredArgsConstructor
@Slf4j
public class TemplateController {

    private final TemplateService templateService;

    @Operation(summary = "Upload file with template to the object storage")
    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public void upload(@ModelAttribute TemplateRequestDTO templateRequestDTO) {
        templateService.createTemplate(templateRequestDTO);
    }

    @Operation(summary = "Get file with template from the object storage")
    @GetMapping("/download")
    public TemplateResponseDTO download(@RequestParam String filename) {
        return templateService.getTemplate(filename);
    }

}
