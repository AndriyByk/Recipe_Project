package com.example.recipe_project.dao.mediate_dao;

import com.example.recipe_project.models.entity.entities.FavoriteRecipe;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IFavoriteRecipeDAO extends JpaRepository <FavoriteRecipe, Integer> {
}
