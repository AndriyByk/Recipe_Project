package com.example.recipe_project.models.dto.ingredient_dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
@JsonIgnoreProperties(value = {"hibernateInitializer"})
public class Ingredient_DTO {
    private String name;
    private String about;
    private String type;

//    public Ingredient_DTO(Ingredient ingredient) {
//        this.name = ingredient.getName();
//        this.about = ingredient.getAbout();
//        this.type = ingredient.getType();
//        List<Nutrient_DTO> nutrientDTOs = new ArrayList<>();
//        List<Nutrient> nutrients = new ArrayList<>(ingredient.getNutrients());
//        for (Nutrient nutrient : nutrients) {
//            nutrientDTOs.add(new Nutrient_DTO(nutrient));
//        }
//        this.nutrients = nutrientDTOs;
//    }
}
