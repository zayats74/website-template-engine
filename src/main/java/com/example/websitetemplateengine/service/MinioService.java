package com.example.websitetemplateengine.service;

import com.example.websitetemplateengine.config.MinioProperties;
import io.minio.GetObjectArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;

@Slf4j
@Service
public class MinioService {

    private final MinioClient minioClient;
    private final MinioProperties minioProperties;

    public MinioService(MinioClient minioClient, MinioProperties minioProperties) {
        this.minioClient = minioClient;
        this.minioProperties = minioProperties;
    }


    public void putObject(String objectName, InputStream inputStream) {
        try{
            minioClient.putObject(PutObjectArgs.builder().bucket(minioProperties.getBucketName()).
                    object(objectName).stream(inputStream, -1, 10485760)
                    .build());
        }
        catch(Exception e){
            log.error("Failed to upload file {}", objectName, e);
            throw new RuntimeException("Failed to upload file", e);
        }
        finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                }
                catch (IOException e) {
                    log.error("Failed to close InputStream", e);
                }
            }
        }
    }

    public String getObject(String objectName) {
        try(InputStream stream = minioClient.getObject(GetObjectArgs.builder()
                .bucket(minioProperties.getBucketName())
                .object(objectName)
                .build())) {
            return new String(stream.readAllBytes());
        }
        catch (Exception e) {
            log.error("Failed to read file {}", objectName, e);
            throw new RuntimeException("Failed to read file" + objectName, e);
        }
    }
}
