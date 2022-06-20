package com.example.recipe_project.services;

import com.example.recipe_project.dao.IIngredientDAO;
import com.example.recipe_project.dao.INutrientDAO;
import com.example.recipe_project.dao.INutrientWithQuantityDAO;
import com.example.recipe_project.dao.IRecipeDAO;
import com.example.recipe_project.models.dto.recipe_dto.RecipeWithRawIngredients;
import com.example.recipe_project.models.entity.*;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class RecipeService {
    private IRecipeDAO recipeDAO;
    private IIngredientDAO ingredientDAO;
    private INutrientDAO nutrientDAO;
    private INutrientWithQuantityDAO nutrientWithQuantityDAO;

    public void saveRecipe(RecipeWithRawIngredients recipeWithRawIngredients) {

        List<IngredientWithWeight> ingredientsReady = new ArrayList<>();
        List<Quantity> quantities = new ArrayList<>();
        Recipe recipe = new Recipe(
                recipeWithRawIngredients.getImage(),
                recipeWithRawIngredients.getTitle(),
                recipeWithRawIngredients.getDescription(),
                recipeWithRawIngredients.getCategory(),
                recipeWithRawIngredients.getRating(),
                ingredientsReady,
                quantities);

        recipeWithRawIngredients.getRawIngredientWithWeights().stream().forEach(ingredientWithWeight -> {
            // для кожного інгредієнта:

            // інгредієнт
            Ingredient ingredient = ingredientDAO.findById(ingredientWithWeight.getId()).get();
 
            // його вага
            int weight = ingredientWithWeight.getWeight();
            ///////////////////

            for (NutrientWithQuantity nutrientFromDB : nutrientWithQuantityDAO.findAll()) {
                Nutrient defNutrient = nutrientDAO.findById(nutrientFromDB.getNutrient().getId()).get();
                double quantity = nutrientFromDB.getQuantity() * weight / 100;
                ingredient.getQuantities().add(new Quantity(defNutrient, ingredient, quantity, recipe));
            }

            recipe.getIngredientWithWeights().add(new IngredientWithWeight(ingredient, recipe, weight));
        });

        recipeDAO.save(recipe);
    }

    ;
}