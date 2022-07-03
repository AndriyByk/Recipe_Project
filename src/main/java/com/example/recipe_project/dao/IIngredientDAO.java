package com.example.recipe_project.dao;

import com.example.recipe_project.models.entity.entities.Ingredient;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IIngredientDAO extends JpaRepository<Ingredient, Integer> {
}
