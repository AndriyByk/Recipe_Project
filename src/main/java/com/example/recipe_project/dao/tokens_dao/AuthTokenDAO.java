package com.example.recipe_project.dao.tokens_dao;

import com.example.recipe_project.models.entity.auth.AuthToken;
import com.example.recipe_project.models.entity.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;
@Transactional
public interface AuthTokenDAO extends JpaRepository<AuthToken, Integer> {
    AuthToken findAuthTokenByToken(String token);
    void deleteAuthTokenByToken(String access);

}
