package com.example.recipe_project.dao;

import com.example.recipe_project.models.entity.NutrientWithQuantity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface INutrientWithQuantityDAO extends JpaRepository<NutrientWithQuantity, Integer> {
}
