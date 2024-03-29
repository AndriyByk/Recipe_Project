package com.example.recipe_project.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
public class BeanConfig {
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // Все починається тут!
    // ---1---
//    провайдер, який робить автентифікацію через DAO рівень
    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider(UserDetailsService userDetailsService, PasswordEncoder passwordEncoder) {

        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        // знаходить юзера по юзернейму
        // прошарок який ідентифікує об'єкти які користувач відправляє на бек - шукає в базі даних
//       // завдяки методу loadByUsername в userService - тобто чи взагалі є юзер з таким юзернеймом
        authenticationProvider.setUserDetailsService(userDetailsService);
        // відбувається дешифровка паролю з бази даних - зашифровку дивитись в UserService при створенні нового User
        // перевіряє його пароль
        authenticationProvider.setPasswordEncoder(passwordEncoder);
        // + відбувається перевірка ролей
        return authenticationProvider;
    }

    // ---3---
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        // з яких серверів можна робити запити на бек
        corsConfiguration.setAllowedOrigins(Arrays.asList("http://localhost:4200","http://localhost:3000", "https://recipe-hub-frontend.herokuapp.com"));
        // доступні всі хедери
        corsConfiguration.addAllowedHeader("*");
        // якими методами можемо робити запити
        corsConfiguration.setAllowedMethods(Arrays.asList(
                HttpMethod.GET.name(),
                HttpMethod.PATCH.name(),
                HttpMethod.POST.name(),
                HttpMethod.DELETE.name(),
                HttpMethod.HEAD.name()
        ));
        // які додаткові хедери будуть видимі клієнту
        corsConfiguration.addExposedHeader("Authorization");
        corsConfiguration.addExposedHeader("Error");
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//        "/**" - будь яка урла
        source.registerCorsConfiguration("/**", corsConfiguration);
        return source;
    }
}
