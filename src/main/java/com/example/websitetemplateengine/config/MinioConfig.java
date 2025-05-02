package com.example.websitetemplateengine.config;

import io.minio.MinioClient;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
@RequiredArgsConstructor
public class MinioConfig {

    private final MinioProperties minioProperties;

    @Bean
    public MinioClient minioClient(){
        return MinioClient.builder()
                .credentials(minioProperties.getUser(), minioProperties.getPassword())
                .endpoint(minioProperties.getMinioUrl(), 9000, minioProperties.isSecure())
                .build();
    }
}
