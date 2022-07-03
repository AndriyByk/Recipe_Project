package com.example.recipe_project.models.entity.raw;

import lombok.*;

import java.util.List;

@NoArgsConstructor
@Getter
@Setter
@AllArgsConstructor
public class RawIngredient {
    private String name;
    private String about;
    private int ingredientCategoryId;
    @ToString.Exclude
    private List<RawNutrientWithQuantity> rawNutrients;
}
