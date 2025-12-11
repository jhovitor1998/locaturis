package com.example.locaturis;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Value("${app.upload.dir}")
    private String uploadDir;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Cria um link mágico: tudo que for acessado em /uploads/ vai buscar na pasta do disco
        // O "file:///" é necessário para indicar que é no sistema de arquivos
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations("file:///" + uploadDir);
    }
}
