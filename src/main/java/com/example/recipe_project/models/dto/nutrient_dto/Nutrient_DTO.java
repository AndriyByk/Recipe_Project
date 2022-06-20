package com.example.recipe_project.models.dto.nutrient_dto;

import com.example.recipe_project.models.entity.Nutrient;
import com.example.recipe_project.models.entity.NutrientWithQuantity;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

//@NoArgsConstructor
//@Getter
//@Setter
//@JsonIgnoreProperties(value = {"hibernateInitializer"})
//public class Nutrient_DTO {
//    private String name;
//    private String category;
//    private String about;
//    private int quantity;
//
//    public Nutrient_DTO(NutrientWithQuantity nutrientWithQuantity) {
//        this.name = nutrientWithQuantity.getNutrient().getName();
//        this.category = nutrientWithQuantity.getNutrient().getCategory();
//        this.about = nutrientWithQuantity.getNutrient().getAbout();
//        this.quantity = nutrientWithQuantity.getQuantity();
//    }
//}
