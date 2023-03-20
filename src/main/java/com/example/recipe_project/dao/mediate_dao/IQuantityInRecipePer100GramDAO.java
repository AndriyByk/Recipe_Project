package com.example.recipe_project.dao.mediate_dao;

import com.example.recipe_project.models.entity.entities.NutrientQuantityInRecipePer100Gramm;
import com.example.recipe_project.models.entity.entities.Recipe;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IQuantityInRecipePer100GramDAO extends JpaRepository<NutrientQuantityInRecipePer100Gramm, Integer> {
    NutrientQuantityInRecipePer100Gramm findByRecipeAndNutrientId(Recipe recipe, int nutrient_id);
}
