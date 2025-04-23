package com.example.websitetemplateengine.service;

import io.minio.GetObjectArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;

@Slf4j
@Service
public class MinioService {

    private final MinioClient minioClient;

    @Value("${minio.bucketName}")
    private String bucketName;

    public MinioService(MinioClient minioClient) {
        this.minioClient = minioClient;
    }

    public void putObject(String objectName, InputStream inputStream) {
        try{
            minioClient.putObject(PutObjectArgs.builder().bucket(bucketName).
                    object(objectName).stream(inputStream, -1, 10485760)
                    .build());
        }
        catch(Exception e){
            log.error(e.getMessage());
        }
        finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                }
                catch (IOException e) {
                    log.error(e.getMessage());
                }
            }
        }
    }

    public String getObject(String objectName) {
        try(InputStream stream = minioClient.getObject(GetObjectArgs.builder()
                .bucket(bucketName)
                .object(objectName)
                .build())){
            return new String(stream.readAllBytes());
        }
        catch (Exception e){
            log.error(e.getMessage());
        }
        return "Storage is empty. Load some files.";
    }
}
