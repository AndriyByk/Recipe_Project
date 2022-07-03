package com.example.recipe_project.dao.categories_dao;

import com.example.recipe_project.models.entity.categories.ActivityType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IActivityTypeDAO extends JpaRepository<ActivityType, Integer> {
}
