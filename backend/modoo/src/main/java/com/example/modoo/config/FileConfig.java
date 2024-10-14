package com.example.modoo.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FileConfig {

    @Value("${file.upload-dir}")
    private String uploadDir;

    @Bean
    public String fileUploadPath() {
        return uploadDir;
    }
}
