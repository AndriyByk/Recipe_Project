package com.example.recipe_project.models.entity.raw;

import lombok.*;

import java.util.List;

@NoArgsConstructor
@Getter
@Setter
@AllArgsConstructor
public class RawRecipe {
    private String title;
    private String description;
    private String dateOfCreation;
    private int recipeCategoryId;
    private List<RawIngredientWithWeight> rawIngredientWithWeights;
}
