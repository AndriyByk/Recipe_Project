package com.example.recipe_project.dao.categories_dao;

import com.example.recipe_project.models.entity.categories.RecipeCategory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IRecipeCategoryDAO extends JpaRepository<RecipeCategory, Integer> {
}
