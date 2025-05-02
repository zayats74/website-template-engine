package com.example.websitetemplateengine.service;

import com.example.websitetemplateengine.dto.TemplateRequestDTO;
import com.example.websitetemplateengine.dto.TemplateResponseDTO;


public interface TemplateService {
    TemplateResponseDTO getTemplate(String path);
    void createTemplate(TemplateRequestDTO template);
}
