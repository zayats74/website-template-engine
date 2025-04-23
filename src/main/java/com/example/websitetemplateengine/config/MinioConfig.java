package com.example.websitetemplateengine.config;

import io.minio.MinioClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class MinioConfig {
    @Value("${minio.server.url}")
    private String minioUrl;

    @Value("${minio.root.user}")
    private String user;

    @Value("${minio.root.password}")
    private String password;

    @Value("${minio.bucketName}")
    private String bucketName;

    @Value("${minio.secure}")
    private boolean minioSecure;

    @Bean
    public MinioClient minioClient(){
        return MinioClient.builder()
                .credentials(user, password)
                .endpoint(minioUrl, 9000, minioSecure)
                .build();
    }
}
