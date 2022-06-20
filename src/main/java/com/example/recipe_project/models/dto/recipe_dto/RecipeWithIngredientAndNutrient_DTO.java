package com.example.recipe_project.models.dto.recipe_dto;

import com.example.recipe_project.models.dto.ingredient_dto.IngredientWithWeight_DTO;
import com.example.recipe_project.models.entity.Recipe;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor
@Getter
@Setter
//@JsonIgnoreProperties(value = {"hibernateInitializer"})
public class RecipeWithIngredientAndNutrient_DTO {
    ////
    private int id;
    ////
    private String image;
    private String title;
    private String description;
    private String category;
    private double rating;
    private List<IngredientWithWeight_DTO> ingredients;

    public RecipeWithIngredientAndNutrient_DTO(Recipe recipe) {
        this.id = recipe.getId();
        this.image = recipe.getImage();
        this.title = recipe.getTitle();
        this.description = recipe.getDescription();
        this.category = recipe.getCategory();
        this.rating = recipe.getRating();
        this.ingredients = recipe
                .getIngredientWithWeights()
                .stream()
                .map(ingredientWithWeight -> new IngredientWithWeight_DTO(
                        ingredientWithWeight.getIngredient().getId(),
                        ingredientWithWeight.getIngredient().getName(),
                        ingredientWithWeight.getIngredient().getAbout(),
                        ingredientWithWeight.getIngredient().getType(),
                        ingredientWithWeight.getWeight(),
                        ingredientWithWeight.getIngredient().getNutrientWithQuantities()))
                .collect(Collectors.toList());
    }
}
