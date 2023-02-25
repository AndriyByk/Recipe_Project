package com.example.recipe_project.dao.entities_dao;

import com.example.recipe_project.models.entity.categories.RecipeCategory;
import com.example.recipe_project.models.entity.entities.Recipe;
import com.example.recipe_project.models.entity.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.List;

public interface IRecipeDAO extends JpaRepository<Recipe, Integer> {
    List<Recipe> findAllByTitleContainingAndCategory (String title, RecipeCategory category);
    List<Recipe> findAllByAuthorAndTitleContainingAndCategory (User user, String title, RecipeCategory category);

    List<Recipe> findAllByTitleContaining (String title);
    List<Recipe> findAllByAuthorAndTitleContaining (User user, String title);

    List<Recipe> findAllByCategory(RecipeCategory category);
    List<Recipe> findAllByAuthorAndCategory(User user, RecipeCategory category);

    List<Recipe> findAllByAuthor(User user);

}
