package com.example.recipe_project.dao.mediate_dao;

import com.example.recipe_project.models.entity.entities.Ingredient;
import com.example.recipe_project.models.entity.entities.Quantity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IQuantityDAO extends JpaRepository<Quantity, Integer> {
    List<Quantity> findByIngredient (Ingredient ingredient);
}
