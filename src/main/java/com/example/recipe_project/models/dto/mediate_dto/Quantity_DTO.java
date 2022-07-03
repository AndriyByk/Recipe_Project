package com.example.recipe_project.models.dto.mediate_dto;

import com.example.recipe_project.models.entity.entities.Quantity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Quantity_DTO {
    private String nameOfNutrient;
    private double quantity;


    public Quantity_DTO(Quantity quantity) {
        this.quantity = quantity.getQuantity();
        this.nameOfNutrient = quantity.getNutrient().getName();
    }
}
