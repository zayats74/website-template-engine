package com.example.websitetemplateengine.service;

import com.example.websitetemplateengine.config.MinioProperties;
import com.example.websitetemplateengine.dto.TemplateRequestDTO;
import com.example.websitetemplateengine.dto.TemplateResponseDTO;
import com.example.websitetemplateengine.entity.Template;
import com.example.websitetemplateengine.entity.User;
import com.example.websitetemplateengine.repository.TemplateRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

@Service
@RequiredArgsConstructor
@Slf4j
public class TemplateServiceImpl implements TemplateService {

    private final TemplateRepository templateRepository;
    private final MinioService minioService;
    private final MinioProperties minioProperties;


    @Override
    public TemplateResponseDTO getTemplate(String path) {
        log.info("Downloading file: " + path.substring(path.lastIndexOf("/") + 1));
        return TemplateResponseDTO.builder().template(minioService.getObject(path)).build();
    }

    @Override
    public void createTemplate(TemplateRequestDTO template) {
        try{
            InputStream in = new ByteArrayInputStream(template.getFile().getBytes());
            String filepath = "public/" + template.getUsername() + "/" + template.getFile().getOriginalFilename();
            minioService.putObject(filepath, in);
            Template t = Template.builder()
                    .name(template.getFile().getOriginalFilename())
                    .bucketName(minioProperties.getBucketName())
                    .key(filepath)
                    .user(User.builder().username(template.getUsername()).build()) //later fix
                    .build();
            templateRepository.save(t);
            log.info("Uploaded file: " + filepath + " by " + template.getUsername());
        }
        catch (Exception e) {
            log.error(e.getMessage());
        }
    }
}
