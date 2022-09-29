package com.example.recipe_project.models.dto.entities_dto;

import com.example.recipe_project.models.dto.categories_dto.NutrientCategory_DTO;
import com.example.recipe_project.models.entity.entities.Nutrient;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Nutrient_DTO {
    private int idOfNutrient;
    private String name;
    private String about;
    private NutrientCategory_DTO categoryDto;
    private double quantity;
    public Nutrient_DTO(Nutrient nutrient, double quantity) {
        this.idOfNutrient = nutrient.getId();
        this.name = nutrient.getName();
        this.about = nutrient.getAbout();
        this.categoryDto = new NutrientCategory_DTO(nutrient.getNutrientCategory());
        this.quantity = quantity;
    }

    public Nutrient_DTO(Nutrient nutrient) {
        this.idOfNutrient = nutrient.getId();
        this.name = nutrient.getName();
        this.about = nutrient.getAbout();
        this.categoryDto = new NutrientCategory_DTO(nutrient.getNutrientCategory());
    }
}
