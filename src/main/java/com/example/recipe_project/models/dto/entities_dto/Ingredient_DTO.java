package com.example.recipe_project.models.dto.entities_dto;

import com.example.recipe_project.models.dto.categories_dto.IngredientCategory_DTO;
import com.example.recipe_project.models.dto.mediate_dto.Quantity_DTO;
import com.example.recipe_project.models.entity.entities.Ingredient;
import lombok.*;

import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor
@Getter
@Setter
@AllArgsConstructor
public class Ingredient_DTO {
    private String name;
    private String about;
    private IngredientCategory_DTO ingredientCategoryDto;
    private List<Quantity_DTO> quantitiesDto;

    public Ingredient_DTO(Ingredient ingredient) {
        this.name = ingredient.getName();
        this.about = ingredient.getAbout();
        this.ingredientCategoryDto = new IngredientCategory_DTO(ingredient.getIngredientCategory());
        this.quantitiesDto = ingredient
                .getQuantities()
                .stream()
                .map(Quantity_DTO::new)
                .collect(Collectors.toList());
    }
}
