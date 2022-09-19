package com.example.recipe_project.models.dto.entities_dto;

import com.example.recipe_project.models.dto.categories_dto.IngredientCategory_DTO;
import com.example.recipe_project.models.dto.categories_dto.RecipeCategory_DTO;
import com.example.recipe_project.models.dto.mediate_dto.Weight_DTO;
import com.example.recipe_project.models.dto.mediate_dto.NutrientQuantitiesInRecipe_DTO;
import com.example.recipe_project.models.entity.entities.Recipe;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor
@Getter
@Setter
public class Recipe_DTO {
    private int id;
    private String image;
    private String title;
    private String description;
    private RecipeCategory_DTO recipeCategoryDto;
//    private double rating;
    private List<Weight_DTO> ingredients;
    private List<NutrientQuantitiesInRecipe_DTO> quantities;
    private UserAuthor_DTO author;


    public Recipe_DTO(Recipe recipe) {
        this.id = recipe.getId();
        this.image = recipe.getImage();
        this.title = recipe.getTitle();
        this.description = recipe.getDescription();
        this.recipeCategoryDto = new RecipeCategory_DTO(recipe.getCategory());
        //        this.rating = recipe.getRating();
        this.author = new UserAuthor_DTO(recipe.getAuthor());
        this.ingredients = recipe
                .getWeights()
                .stream()
                .map(weight -> new Weight_DTO(
                        weight.getIngredient().getId(),
                        weight.getIngredient().getName(),
                        weight.getIngredient().getAbout(),
                        new IngredientCategory_DTO(weight.getIngredient().getIngredientCategory()),
                        weight.getWeight()
//                        weight.getIngredient().getQuantities())
                        ))
                .collect(Collectors.toList());
        this.quantities = recipe
                .getNutrientQuantities()
                .stream()
                .map(quantity -> new NutrientQuantitiesInRecipe_DTO(
                    new Nutrient_DTO(quantity.getNutrient(), quantity.getQuantity())))
                .collect(Collectors.toList());
    }
}
