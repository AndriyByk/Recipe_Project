package com.example.recipe_project.models.entity.raw;

import lombok.*;

import java.util.List;

@NoArgsConstructor
@Getter
@Setter
@AllArgsConstructor
public class RawRecipe {
//    private String image;
    private String title;
    private String description;
    private int recipeCategoryId;
//    private double rating;
    private List<RawIngredientWithWeight> rawIngredientWithWeights;
}
