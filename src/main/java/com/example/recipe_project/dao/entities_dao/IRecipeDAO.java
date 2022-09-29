package com.example.recipe_project.dao.entities_dao;

import com.example.recipe_project.models.entity.categories.RecipeCategory;
import com.example.recipe_project.models.entity.entities.Recipe;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IRecipeDAO extends JpaRepository<Recipe, Integer> {
    Recipe findByTitle (String title);
    List<Recipe> findByTitleContainingAndCategory (String title, RecipeCategory category);
    List<Recipe> findByTitleContaining (String title);
    List<Recipe> findByCategory(RecipeCategory category);

}
