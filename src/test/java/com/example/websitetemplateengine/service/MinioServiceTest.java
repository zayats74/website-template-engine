package com.example.websitetemplateengine.service;

import io.minio.GetObjectArgs;
import io.minio.GetObjectResponse;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.errors.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MinioServiceTest {

    @Mock
    private MinioClient minioClient;

    @InjectMocks
    private MinioService minioService;

    private final String bucketName = "template-bucket";
    private final String objectName = "test.html";
    private final String testContent = "Hello, World!";

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(minioService, "bucketName", bucketName);
    }

    @Test
    void putObject_ShouldSuccessfullyPutObject() throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        InputStream in = new ByteArrayInputStream(testContent.getBytes());

        minioService.putObject(objectName, in);

        verify(minioClient).putObject(any(PutObjectArgs.class));
    }

    @Test
    void putObject_ShouldCloseStreamOnError() throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        InputStream in = mock(InputStream.class);

        doThrow(new RuntimeException("Minio error")).when(minioClient).putObject(any(PutObjectArgs.class));

        minioService.putObject(objectName, in);

        verify(in).close();
    }

    @Test
    void getObject_ShouldReturnContentWhenObjectExists() throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        GetObjectResponse response = mock(GetObjectResponse.class);
        when(response.readAllBytes()).thenReturn(testContent.getBytes());
        when(minioClient.getObject(any(GetObjectArgs.class))).thenReturn(response);

        String res = minioService.getObject(objectName);

        assertEquals(testContent, res);
        verify(minioClient).getObject(any(GetObjectArgs.class));
    }

    @Test
    void getObject_ShouldHandleIOException() throws Exception{
        GetObjectResponse response = mock(GetObjectResponse.class);
        when(response.readAllBytes()).thenThrow(new IOException("Read error"));
        when(minioClient.getObject(any(GetObjectArgs.class))).thenReturn(response);

        String res = minioService.getObject(objectName);

        assertEquals("Storage is empty. Load some files.", res);
    }
}
