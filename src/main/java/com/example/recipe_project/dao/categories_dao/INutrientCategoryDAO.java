package com.example.recipe_project.dao.categories_dao;

import com.example.recipe_project.models.entity.categories.NutrientCategory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface INutrientCategoryDAO extends JpaRepository<NutrientCategory, Integer> {
}
