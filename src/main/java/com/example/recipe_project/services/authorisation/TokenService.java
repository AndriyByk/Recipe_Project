package com.example.recipe_project.services.authorisation;

import com.example.recipe_project.dao.entities_dao.IUserDAO;
import com.example.recipe_project.dao.tokens_dao.AuthTokenDAO;
import com.example.recipe_project.models.entity.auth.AuthToken;
import com.example.recipe_project.models.entity.entities.User;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;

@Service
@AllArgsConstructor
public class TokenService {
    private AuthTokenDAO authTokenDAO;
    private IUserDAO userDAO;
    public void deleteAuthTokenByUser_Username(String access) {
        Set<AuthToken> authTokens = new HashSet<>();
        AuthToken token = authTokenDAO.findAuthTokenByToken(access);
        authTokens.add(token);
        User user = userDAO.findFirstByAuthTokensIn(authTokens);

        user.getAuthTokens().remove(token);
        authTokenDAO.deleteAuthTokenByToken(access);
    }
}
