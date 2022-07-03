package com.example.recipe_project.models.dto.mediate_dto;

import com.example.recipe_project.models.dto.entities_dto.Nutrient_DTO;
import com.example.recipe_project.models.entity.entities.NutrientQuantityInRecipe;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class NutrientQuantitiesInRecipe_DTO {
    private Nutrient_DTO nutrientDto;
    private double quantity;

    public NutrientQuantitiesInRecipe_DTO(NutrientQuantityInRecipe recipe) {
        this.nutrientDto = new Nutrient_DTO(recipe.getNutrient());
        this.quantity = recipe.getQuantity();
    }
}
