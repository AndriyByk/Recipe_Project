package com.example.recipe_project.dao.entities_dao;

import com.example.recipe_project.models.entity.entities.Nutrient;
import org.springframework.data.jpa.repository.JpaRepository;

public interface INutrientDAO extends JpaRepository<Nutrient, Integer> {
}
