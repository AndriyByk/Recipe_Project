package com.example.recipe_project.models.dto.categories_dto;

import com.example.recipe_project.models.entity.categories.RecipeCategory;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RecipeCategory_DTO {
    private String name;

    public RecipeCategory_DTO(RecipeCategory category) {
        this.name = category.getName();
    }
}
