package com.example.recipe_project.configs;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;

@Configuration
@AllArgsConstructor
public class DaoAuthenticationProviderConfig {
    private DaoAuthenticationProvider daoAuthenticationProvider;

    // ---4---
    @Bean
    public AuthenticationManager authenticationManager() {
        return new ProviderManager(daoAuthenticationProvider);
    }
}
