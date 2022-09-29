package com.example.recipe_project.dao.mediate_dao;

import com.example.recipe_project.models.entity.entities.NutrientQuantityInRecipePer100Gramm;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IQuantityInRecipePer100GramDAO extends JpaRepository<NutrientQuantityInRecipePer100Gramm, Integer> {
    List<NutrientQuantityInRecipePer100Gramm> findByNutrientIdOrderByQuantityDesc (int nutrient_id);
}
