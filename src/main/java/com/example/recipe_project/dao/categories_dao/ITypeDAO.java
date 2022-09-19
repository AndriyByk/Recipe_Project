package com.example.recipe_project.dao.categories_dao;

import com.example.recipe_project.models.entity.categories.norm.Type;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ITypeDAO extends JpaRepository<Type, Integer> {
}
