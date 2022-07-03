package com.example.recipe_project.dao;

import com.example.recipe_project.models.entity.entities.Quantity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IQuantityDAO extends JpaRepository<Quantity, Integer> {
}
