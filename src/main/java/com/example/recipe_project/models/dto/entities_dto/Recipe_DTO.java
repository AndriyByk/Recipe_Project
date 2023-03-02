package com.example.recipe_project.models.dto.entities_dto;

import com.example.recipe_project.models.dto.categories_dto.IngredientCategory_DTO;
import com.example.recipe_project.models.dto.categories_dto.RecipeCategory_DTO;
import com.example.recipe_project.models.dto.mediate_dto.NutrientQuantitiesInRecipePer100_DTO;
import com.example.recipe_project.models.dto.mediate_dto.Ranking_DTO;
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
    private String dateOfCreation;
    private double rating;
    private String status;
    private RecipeCategory_DTO recipeCategoryDto;
//    private double rating;
    private List<Weight_DTO> ingredients;
    private List<NutrientQuantitiesInRecipe_DTO> quantities;
    private List<NutrientQuantitiesInRecipePer100_DTO> quantitiesPer100;
    private UserAuthor_DTO author;
    private List<Ranking_DTO> ranks;
    private List<Comment_DTO> comments;

    public Recipe_DTO(Recipe recipe) {
        this.id = recipe.getId();
        this.image = recipe.getImage();
        this.title = recipe.getTitle();
        this.description = recipe.getDescription();
        this.dateOfCreation = recipe.getDateOfCreation();
        this.rating = recipe.getRating();
        this.status = recipe.getStatus().name();
        this.recipeCategoryDto = new RecipeCategory_DTO(recipe.getCategory());
        this.author = new UserAuthor_DTO(recipe.getAuthor());
        this.ingredients = recipe
                .getWeights()
                .stream()
                .map(weight -> new Weight_DTO(
                        weight.getIngredient().getId(),
                        weight.getIngredient().getName(),
                        weight.getIngredient().getAbout(),
                        weight.getIngredient().getName_ukr(),
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
        this.quantitiesPer100 = recipe
                .getNutrientQuantitiesPer100Gram()
                .stream()
                .map(quantityPer100 -> new NutrientQuantitiesInRecipePer100_DTO(
                        new Nutrient_DTO(quantityPer100.getNutrient(), quantityPer100.getQuantity())))
                .collect(Collectors.toList());
        this.ranks = recipe
                .getRankings()
                .stream()
                .map(Ranking_DTO::new)
                .collect(Collectors.toList());
        this.comments = recipe
                .getComments()
                .stream()
                .map(Comment_DTO::new)
                .collect(Collectors.toList());
    }
}
