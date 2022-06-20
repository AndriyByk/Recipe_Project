package com.example.recipe_project.models.dto.ingredient_dto;

import com.example.recipe_project.models.entity.NutrientWithQuantity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class IngredientWithWeight_DTO {
    private int id;
    private String name;
    private String about;
    private String type;
    private int weight;
    private List<NutrientWithQuantity> nutrients;
}
