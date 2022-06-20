package com.example.recipe_project.dao;

import com.example.recipe_project.models.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IUserDAO extends JpaRepository<User, Integer> {
}
