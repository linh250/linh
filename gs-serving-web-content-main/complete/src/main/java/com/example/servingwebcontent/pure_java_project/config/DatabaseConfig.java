package com.example.servingwebcontent.pure_java_project.config;

import com.example.servingwebcontent.pure_java_project.database.AivenConnection;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DatabaseConfig {

    @Bean
    public AivenConnection aivenConnection(AivenConnection conn) {
        return conn;
    }
}