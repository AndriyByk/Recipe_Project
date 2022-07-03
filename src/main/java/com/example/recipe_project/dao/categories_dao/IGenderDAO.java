package com.example.recipe_project.dao.categories_dao;

import com.example.recipe_project.models.entity.categories.Gender;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IGenderDAO extends JpaRepository<Gender, Integer> {
}
