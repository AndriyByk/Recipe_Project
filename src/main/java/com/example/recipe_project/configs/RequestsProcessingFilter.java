package com.example.recipe_project.configs;

import com.example.recipe_project.dao.entities_dao.IUserDAO;
import com.example.recipe_project.dao.tokens_dao.AuthTokenDAO;
import com.example.recipe_project.models.entity.auth.AuthToken;
import com.example.recipe_project.models.entity.entities.User;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class RequestsProcessingFilter extends GenericFilterBean {
    private final AuthTokenDAO authTokenDAO;

    public RequestsProcessingFilter(AuthTokenDAO authTokenDAO) {
        this.authTokenDAO = authTokenDAO;
    }

    // запит повинен вже мати токен в хедері \|/
    @Override
    public void doFilter(
            ServletRequest servletRequest,
            ServletResponse servletResponse,
            FilterChain filterChain) throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) servletRequest;
        String authorization = request.getHeader("Authorization");
        if (authorization != null && authorization.startsWith("Bearer ")) {
            String bearer = authorization.replace("Bearer ", "");
            AuthToken userToken = authTokenDAO.findAuthTokenByToken(bearer);
            User user = userToken.getUser();

            if (user.getAuthTokens().contains(userToken)) {
                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                        user.getUsername(),
                        user.getPassword(),
                        user.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }
        }
        filterChain.doFilter(servletRequest, servletResponse);
    }
}
