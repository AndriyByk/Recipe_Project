package com.example.recipe_project.models.entity.raw;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
@AllArgsConstructor
public class RawRecipeWithUserRate {
    private int userId;
    private int recipeId;
    private int rate;
}
