package com.example.recipe_project.dao.entities_dao;

import com.example.recipe_project.models.entity.categories.norm.Type;
import com.example.recipe_project.models.entity.entities.Norm;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Set;

public interface INormDAO extends JpaRepository<Norm, Integer> {
    Set<Norm> findByAgeBetweenAndWeightAndSexAndType(double ageSmall, double ageBig, int weight, String sex, Type type);
}
