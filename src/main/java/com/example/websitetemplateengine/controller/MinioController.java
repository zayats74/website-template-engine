package com.example.websitetemplateengine.controller;

import com.example.websitetemplateengine.service.MinioService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;


@Slf4j
@RestController
@RequestMapping("/api/v1/minio")
@RequiredArgsConstructor
public class MinioController {

    private final MinioService minioService;


    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public void upload(@RequestParam("file") MultipartFile file) {
        try {
            InputStream in = new ByteArrayInputStream(file.getBytes());
            String fileName = "public/system/" + file.getOriginalFilename();
            minioService.putObject(fileName, in);
            log.info("Uploaded File: {}", fileName);
        }
        catch (IOException e){
            log.error("Failed to read file: {}", file.getOriginalFilename());
            throw new RuntimeException("Failed to read file: " + file.getOriginalFilename(), e);
        }
    }

    @GetMapping("/download")
    public String download(@RequestParam String file) {
        log.info("Downloading File: {}", file);
        return minioService.getObject(file);
    }
}
