package com.example.recipe_project.dao.mediate_dao;

import com.example.recipe_project.models.entity.entities.FavoriteRecipe;
import com.example.recipe_project.models.entity.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IFavoriteRecipeDAO extends JpaRepository <FavoriteRecipe, Integer> {
    List<FavoriteRecipe> findAllByUser(User user);
}
