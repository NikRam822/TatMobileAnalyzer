package com.example.TatMobileAnalyzer.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebMvc
public class CorsConfig implements WebMvcConfigurer {

    @Value("${frontend.host}")
    private String host;

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins(host)
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("Accept",
                        "Content-Length",
                        "Accept-Encoding",
                        "X-CSRF-Token",
                        "Access-Control-Allow-Origin",
                        "Authorization",
                        "Content-Type",
                        "Access-Control-Expose-Headers",
                        "Access-Control-Allow-Headers",
                        "Set-Cookie")
                .allowCredentials(true);
    }
}