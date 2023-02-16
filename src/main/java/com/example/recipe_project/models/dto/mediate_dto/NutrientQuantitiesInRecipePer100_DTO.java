package com.example.recipe_project.models.dto.mediate_dto;

import com.example.recipe_project.models.dto.entities_dto.Nutrient_DTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class NutrientQuantitiesInRecipePer100_DTO {
    private Nutrient_DTO nutrientDto;
}
