package com.example.recipe_project.models.dto.mediate_dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserNorm_DTO {
    private int idOfNutrient;
    private String engNameOfNutrient;
    private String ukrNameOfNutrient;
    private String unit;
    private double quantity;
}
