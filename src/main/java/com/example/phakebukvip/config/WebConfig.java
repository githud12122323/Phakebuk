package com.example.phakebukvip.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        String uploadDir = System.getProperty("user.dir") + "/uploads/";

        registry.addResourceHandler("/uploads/images/**")
                .addResourceLocations("file:" + uploadDir + "images/");

        registry.addResourceHandler("/uploads/videos/**")
                .addResourceLocations("file:" + uploadDir + "videos/");
    }
}

