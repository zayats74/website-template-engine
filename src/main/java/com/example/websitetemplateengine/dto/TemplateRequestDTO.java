package com.example.websitetemplateengine.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;


@Data
@Schema(description = "Request for uploading template")
public class TemplateRequestDTO {
    private String username;

    private MultipartFile file;
}
