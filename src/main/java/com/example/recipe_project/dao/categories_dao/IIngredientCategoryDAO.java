package com.example.recipe_project.dao.categories_dao;

import com.example.recipe_project.models.entity.categories.IngredientCategory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IIngredientCategoryDAO extends JpaRepository<IngredientCategory, Integer> {
}
