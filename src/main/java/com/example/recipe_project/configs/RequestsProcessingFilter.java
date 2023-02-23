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
    private AuthTokenDAO authTokenDAO;

    public RequestsProcessingFilter(AuthTokenDAO authTokenDAO) {
        this.authTokenDAO = authTokenDAO;
    }

    // запит повинен вже мати токен в хедері \|/
    @Override
    public void doFilter(
            ServletRequest servletRequest,
            ServletResponse servletResponse,
            FilterChain filterChain) throws IOException, ServletException {

//        HttpServletResponse response = (HttpServletResponse) servletResponse;
//        //---->
//
////        System.out.println("There are headers names");
////        System.out.println("---------------");
////        response.getHeaderNames().stream().peek(System.out::println).collect(Collectors.toList());
////        System.out.println("---------------");
//        System.out.println("////////////");
//        System.out.println("do contain response Authorization header???");
//        System.out.println(response.containsHeader("Authorization"));
//        System.out.println("////////////");
//        String authorization = response.getHeader("Authorization");
//        if (authorization != null && authorization.startsWith("Bearer ")) {
//            String bearer = authorization.replace("Bearer ", "");
//            //---->
//            System.out.println("bearer " + bearer);
//            AuthToken userToken = authTokenDAO.findAuthTokenByToken(bearer);
//
//
//            Set<AuthToken> authTokens = new HashSet<>();
//            authTokens.add(userToken);
//            User user = userDAO.findFirstByAuthTokensIn(authTokens);
//            //---->
//            System.out.println(user.getUsername());
//            if (userToken != null && user != null && user.getAuthTokens().contains(userToken)) {
//
//                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
//                        user.getUsername(),
//                        user.getPassword(),
//                        user.getAuthorities());
//                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
//            }
//        }

        HttpServletRequest request = (HttpServletRequest) servletRequest;
        String authorization = request.getHeader("Authorization");
        if (authorization != null && authorization.startsWith("Bearer ")) {
            String bearer = authorization.replace("Bearer ", "");
            //---->
            System.out.println("bearer " + bearer);
            AuthToken userToken = authTokenDAO.findAuthTokenByToken(bearer);


//            Set<AuthToken> authTokens = new HashSet<>();
//            authTokens.add(userToken);
//            User user = userDAO.findFirstByAuthTokensIn(authTokens);
            User user = userToken.getUser();
            //---->
            System.out.println(user.getUsername());
//            if (userToken != null && user != null && user.getAuthTokens().contains(userToken)) {
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
