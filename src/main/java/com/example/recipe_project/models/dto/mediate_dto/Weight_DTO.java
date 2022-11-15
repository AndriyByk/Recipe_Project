package com.example.recipe_project.models.dto.mediate_dto;

import com.example.recipe_project.models.dto.categories_dto.IngredientCategory_DTO;
import com.example.recipe_project.models.entity.categories.IngredientCategory;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Weight_DTO {
    private int id;
    private String name;
    private String about;
    private String name_ukr;
    private IngredientCategory_DTO ingredientCategoryDto;
    private int weight;
//    private List<Quantity> quantities;
}
