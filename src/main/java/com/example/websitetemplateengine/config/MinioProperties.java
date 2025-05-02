package com.example.websitetemplateengine.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;


@Configuration
@Getter
@Setter
@PropertySource("classpath:application-local.properties")
@ConfigurationProperties(prefix = "minio")
public class MinioProperties {
    @Value("${minio.server.url}")
    private String minioUrl;

    @Value("${minio.root.user}")
    private String user;

    @Value("${minio.root.password}")
    private String password;

    private String bucketName;

    private boolean secure;
}
