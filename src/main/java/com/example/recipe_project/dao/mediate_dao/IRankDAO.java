package com.example.recipe_project.dao.mediate_dao;

import com.example.recipe_project.models.entity.entities.Ranking;
import com.example.recipe_project.models.entity.entities.Recipe;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IRankDAO extends JpaRepository<Ranking, Integer> {

    List<Ranking> findAllByRecipe (Recipe recipe);
}
