package com.example.recipe_project.configs;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.io.File;

@EnableWebMvc
@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        String path = "File:///" + System.getProperty("user.home") + File.separator
                + "IdeaProjects" + File.separator
                + "Recipe_Project" + File.separator
                + "src" + File.separator
                + "main" + File.separator
                + "java" + File.separator
                + "com" + File.separator
                + "example" + File.separator
                + "recipe_project" + File.separator
                + "pictures" + File.separator
                + "users" + File.separator;
        // урла запиту з фронта, де ** значать назву картинки
        registry.addResourceHandler("/pictures/**").addResourceLocations(path);
    }
}
