package com.example.recipe_project.configs;

import com.example.recipe_project.dao.entities_dao.IUserDAO;
import com.example.recipe_project.dao.tokens_dao.AuthTokenDAO;
import com.example.recipe_project.models.dto.sign_in.UserSignIn_DTO;
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
        System.out.println("SignInFilter " + url);
    }

    @Override
    public Authentication attemptAuthentication(
            HttpServletRequest request,
            HttpServletResponse response) throws AuthenticationException {
        try {
            UserSignIn_DTO userSignIn_dto = new ObjectMapper().readValue(request.getInputStream(), UserSignIn_DTO.class);

            System.out.println("SignInFilter " + userSignIn_dto.getUsername());
            System.out.println("SignInFilter " + userSignIn_dto.getPassword());
            // співставленння з даними в базі даних
            try {
                return getAuthenticationManager().authenticate(new UsernamePasswordAuthenticationToken(
                        userSignIn_dto.getUsername(),
                        userSignIn_dto.getPassword()
                        /*, userSignIn_dto.getRoles()*/
                ));
            } catch (InternalAuthenticationServiceException e){
                response.addHeader("Error", "No such user in database");
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("errrrrrorrrrrrrrrrrrrrrrrrrrrrrrrrrrrr");
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
        userByName.getAuthTokens().add(authToken);
        userService.saveUser(userByName);

        response.addHeader("Authorization", "Bearer " + jwtsToken);
        chain.doFilter(request, response);
    }
}

