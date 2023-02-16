package com.example.recipe_project.dao.entities_dao;

import com.example.recipe_project.models.entity.categories.RecipeCategory;
import com.example.recipe_project.models.entity.entities.Recipe;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.List;

public interface IRecipeDAO extends JpaRepository<Recipe, Integer> {
    Recipe findByTitle (String title);
//    List<Recipe> findAllByTitleContainingAndCategory (String title, RecipeCategory category, PageRequest request);
    List<Recipe> findAllByTitleContainingAndCategory (String title, RecipeCategory category);

//    List<Recipe> findAllByTitleContaining (String title, PageRequest request);
    List<Recipe> findAllByTitleContaining (String title);

//    List<Recipe> findAllByCategory(RecipeCategory category, PageRequest request);
    List<Recipe> findAllByCategory(RecipeCategory category);

}
