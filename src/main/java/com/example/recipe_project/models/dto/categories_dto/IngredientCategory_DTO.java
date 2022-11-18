package com.example.recipe_project.models.dto.categories_dto;

import com.example.recipe_project.models.entity.categories.IngredientCategory;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class IngredientCategory_DTO {
    private String name;
    private int id;

    public IngredientCategory_DTO(IngredientCategory category) {
        this.name = category.getName();
        this.id = category.getId();
    }
}
