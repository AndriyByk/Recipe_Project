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

public class RequestsProcessingFilter extends GenericFilterBean {
    private AuthTokenDAO authTokenDAO;
    private IUserDAO userDAO;

    public RequestsProcessingFilter(AuthTokenDAO authTokenDAO, IUserDAO iUserDAO) {
        this.authTokenDAO = authTokenDAO;
        this.userDAO = iUserDAO;
    }

    // запит повинен вже мати токен в хедері \|/
    @Override
    public void doFilter(
            ServletRequest servletRequest,
            ServletResponse servletResponse,
            FilterChain filterChain) throws IOException, ServletException {

        HttpServletResponse response = (HttpServletResponse) servletResponse;
        String authorization = response.getHeader("Authorization");

        if (authorization != null && authorization.startsWith("Bearer ")) {
            String bearer = authorization.replace("Bearer ", "");
            AuthToken userToken = authTokenDAO.findAuthTokenByToken(bearer);

            Set<AuthToken> authTokens = new HashSet<>();
            authTokens.add(userToken);
            User user = userDAO.findFirstByAuthTokensIn(authTokens);

            if (userToken != null && user != null && user.getAuthTokens().contains(userToken)) {

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
