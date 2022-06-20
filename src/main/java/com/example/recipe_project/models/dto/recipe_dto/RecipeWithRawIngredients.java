package com.example.recipe_project.models.dto.recipe_dto;

import com.example.recipe_project.models.entity.RawIngredientWithWeight;
import lombok.*;

import java.util.List;

//@NoArgsConstructor
//@AllArgsConstructor
//@Getter
//@Setter
//@JsonIgnoreProperties(value = {"hibernateInitializer"})

@Data
public class RecipeWithRawIngredients {
    private String image;
    private String title;
    private String description;
    private String category;
    private double rating;
    private List<RawIngredientWithWeight> rawIngredientWithWeights;
}
