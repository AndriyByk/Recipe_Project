package com.example.recipe_project.configs;

import com.example.recipe_project.models.dto.sign_in_dto.UserSignIn_DTO;
import com.example.recipe_project.models.entity.auth.AuthToken;
import com.example.recipe_project.models.entity.entities.User;
import com.example.recipe_project.services.authorisation.TokenBuilderService;
import com.example.recipe_project.services.entities.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;

@AllArgsConstructor
public class SignInFilter extends UsernamePasswordAuthenticationFilter {
    private UserService userService;
    private TokenBuilderService tokenBuilderService;

    public SignInFilter(
            // url на яку реагує фільтри
            String url,
            AuthenticationManager authenticationManager,
            TokenBuilderService tokenBuilderService,
            UserService userService
    ) {
        setFilterProcessesUrl(url);
        setAuthenticationManager(authenticationManager);
        this.userService = userService;
        this.tokenBuilderService = tokenBuilderService;
    }

    @Override
    public Authentication attemptAuthentication(
            HttpServletRequest request,
            HttpServletResponse response) throws AuthenticationException {
        try {
            UserSignIn_DTO userSignIn_dto = new ObjectMapper().readValue(request.getInputStream(), UserSignIn_DTO.class);

            // співставленння з даними в базі даних
            try {
                // getAuthenticationManager() належить до UsernamePasswordAuthenticationFilter
                // під капотом налаштовується з сукупності  конфігурації daoAuthenticationProvider, cors і тд в нашому BeanConfig
                return getAuthenticationManager().authenticate(new UsernamePasswordAuthenticationToken(
                                // principal - username, credentials - password
                        // якщо дані невалідні - error 401 unauthorized - див. нижче
                        userSignIn_dto.getUsername(),
                        userSignIn_dto.getPassword()
                ));
            } catch (InternalAuthenticationServiceException e){
                response.addHeader("Error", "No such user in database");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    // якшо не знаходить відповідних даних в базі даних то метод не відпрацьовує
    @Override
    protected void successfulAuthentication(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain chain,
            Authentication authResult
    ) throws IOException, ServletException {
        User userByName = userService.findByName(authResult.getName());
        String jwtsToken = tokenBuilderService.createToken(authResult);

        AuthToken authToken = new AuthToken();

        authToken.setToken(jwtsToken);
        authToken.setUser(userByName);

        userByName.getAuthTokens().add(authToken);
        userService.saveUser(userByName);

        response.addHeader("Authorization", "Bearer " + jwtsToken);
        chain.doFilter(request, response);
    }
}

