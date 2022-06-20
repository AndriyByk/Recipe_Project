package com.example.recipe_project.models.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class NutrientWithQuantity {
    @EmbeddedId
    private IdBetweenIngredientAndNutrient id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("nutrient_id")
    private Nutrient nutrient;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("ingredient_id")
    private Ingredient ingredient;

    @Column
    private double quantity;

    public NutrientWithQuantity(Nutrient nutrient, Ingredient ingredient, int quantity) {
        this.nutrient = nutrient;
        this.ingredient = ingredient;
        this.quantity = quantity;
        this.id = new IdBetweenIngredientAndNutrient(ingredient.getId(), nutrient.getId());
    }
}
