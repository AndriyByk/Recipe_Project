package com.example.recipe_project.dao;

import com.example.recipe_project.models.entity.Recipe;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IRecipeDAO extends JpaRepository<Recipe, Integer> {
}
