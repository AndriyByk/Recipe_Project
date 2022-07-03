package com.example.recipe_project.models.dto.categories_dto;

import com.example.recipe_project.models.entity.categories.NutrientCategory;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class NutrientCategory_DTO {
    private String name;

    public NutrientCategory_DTO(NutrientCategory category) {
        this.name = category.getName();
    }
}
