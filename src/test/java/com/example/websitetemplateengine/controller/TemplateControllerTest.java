package com.example.websitetemplateengine.controller;


import com.example.websitetemplateengine.dto.TemplateRequestDTO;
import com.example.websitetemplateengine.dto.TemplateResponseDTO;
import com.example.websitetemplateengine.service.TemplateService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TemplateControllerTest {

    @Mock
    private TemplateService templateService;

    @InjectMocks
    private TemplateController templateController;

    @Test
    void upload_ShouldCallServiceWithCorrectParameters() {
        String username = "system";
        MultipartFile multipartFile = new MockMultipartFile(
                "file",
                "test.html",
                "text/html",
                "<html>Test</html>".getBytes()
        );

        TemplateRequestDTO templateRequestDTO = new TemplateRequestDTO();
        templateRequestDTO.setUsername(username);
        templateRequestDTO.setFile(multipartFile);

        templateController.upload(templateRequestDTO);

        verify(templateService).createTemplate(templateRequestDTO);
        assertEquals(username, templateRequestDTO.getUsername());
        assertEquals(multipartFile, templateRequestDTO.getFile());
    }

    @Test
    public void download_ShouldReturnResponseFromService() {
        String filename = "public/system/test.html";
        String expectedContent = "<html>Test</html>";

        TemplateResponseDTO expectedResponse = TemplateResponseDTO.builder()
                .template(expectedContent).build();

        when(templateService.getTemplate(filename)).thenReturn(expectedResponse);

        TemplateResponseDTO templateResponse = templateController.download(filename);

        verify(templateService).getTemplate(filename);
        assertEquals(expectedResponse, templateResponse);
    }

    @Test
    public void download_ShouldReturnEmptyResponseFromService() {
        String filename = "public/system/aboba.html";

        when(templateService.getTemplate(filename)).thenReturn(null);

        TemplateResponseDTO templateResponse = templateController.download(filename);

        assertNull(templateResponse);
    }
}
