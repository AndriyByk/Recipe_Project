package com.example.recipe_project.dao;

import com.example.recipe_project.models.entity.Nutrient;
import org.springframework.data.jpa.repository.JpaRepository;

public interface INutrientDAO extends JpaRepository<Nutrient, Integer> {
}
