package com.example.recipe_project.models.entity.raw;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@Getter
@Setter
@AllArgsConstructor
public class RawUpdatedRecipe {
    private String description;
    private int recipeCategoryId;
    private List<RawIngredientWithWeight> rawIngredientWithWeights;
}
