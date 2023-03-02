package com.example.recipe_project.dao.entities_dao;

import com.example.recipe_project.models.entity.categories.RecipeCategory;
import com.example.recipe_project.models.entity.categories.Status;
import com.example.recipe_project.models.entity.entities.Recipe;
import com.example.recipe_project.models.entity.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IRecipeDAO extends JpaRepository<Recipe, Integer> {
    List<Recipe> findAllByTitleContainingAndCategory (String title, RecipeCategory category);
    List<Recipe> findAllByStatusInAndTitleContainingAndCategory (List<Status> status, String title, RecipeCategory category);
    List<Recipe> findAllByAuthorAndTitleContainingAndCategory (User user, String title, RecipeCategory category);
    List<Recipe> findAllByStatusInAndAuthorAndTitleContainingAndCategory (List<Status> status, User user, String title, RecipeCategory category);


    List<Recipe> findAllByTitleContaining (String title);
    List<Recipe> findAllByStatusInAndTitleContaining (List<Status> status, String title);

    List<Recipe> findAllByAuthorAndTitleContaining (User user, String title);
    List<Recipe> findAllByStatusInAndAuthorAndTitleContaining (List<Status> status, User user, String title);


    List<Recipe> findAllByCategory(RecipeCategory category);
    List<Recipe> findAllByStatusInAndCategory(List<Status> status, RecipeCategory category);

    List<Recipe> findAllByAuthorAndCategory(User user, RecipeCategory category);
    List<Recipe> findAllByStatusInAndAuthorAndCategory(List<Status> status, User user, RecipeCategory category);

    List<Recipe> findAllByAuthor(User user);
    List<Recipe> findAllByStatusInAndAuthor(List<Status> status, User user);
    List<Recipe> findAllByStatusIn(List<Status> status);




}
