package com.example.recipe_project.dao.entities_dao;

import com.example.recipe_project.models.entity.auth.AuthToken;
import com.example.recipe_project.models.entity.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.List;
import java.util.Set;

public interface IUserDAO extends JpaRepository<User, Integer>, JpaSpecificationExecutor<User> {
    User findByUsername(String username);
    User findFirstByAuthTokensIn(Set<AuthToken> authTokens);
}
