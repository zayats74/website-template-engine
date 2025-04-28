package com.example.websitetemplateengine.controller;

import com.example.websitetemplateengine.service.MinioService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
public class MinioControllerTest {

    @Mock
    private MinioService minioService;

    @InjectMocks
    private MinioController minioController;

    @Test
    void upload_ShouldSuccessfullyUploadFile() throws IOException {
        String fileName = "test.html";
        MultipartFile file = new MockMultipartFile(
                "file",
                "test.html",
                "text/html",
                "<html>Hello World</html>".getBytes()
        );

        minioController.upload(file);

        verify(minioService).putObject(eq("public/system/" + fileName), any(InputStream.class));
    }

    @Test
    void upload_ShouldHandleIOException() throws IOException {
        MultipartFile file = mock(MultipartFile.class);
        when(file.getOriginalFilename()).thenReturn("test.html");
        when(file.getBytes()).thenThrow(new IOException("File read error"));

        minioController.upload(file);

        verify(minioService, never()).putObject(anyString(), any(InputStream.class));
    }

    @Test
    void download_ShouldReturnFileContent() {
        String fileName = "public/system/test.html";
        String expectedContent = "<html>Hello World</html>";
        when(minioService.getObject(fileName)).thenReturn(expectedContent);

        String res = minioController.download(fileName);

        assertEquals(expectedContent, res);
        verify(minioService).getObject(fileName);
    }
}
