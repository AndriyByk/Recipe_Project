package com.example.recipe_project.models.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class IdBetweenIngredientAndNutrient implements Serializable {
    private static final long serialVersionUID = 5862345631407128450L;
    private int ingredient_id;
    private int nutrient_id;
}
