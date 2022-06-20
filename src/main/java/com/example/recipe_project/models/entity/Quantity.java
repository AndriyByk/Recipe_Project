package com.example.recipe_project.models.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class Quantity {
    @EmbeddedId
    private QuantityId id_;
    @ManyToOne(fetch = FetchType.LAZY)
    // назва колонки таблички, яка створюється
    @MapsId("nutrient_id")
    private Nutrient nutrient;

    @ManyToOne(fetch = FetchType.LAZY)
    // назва колонки таблички, яка створюється
    @MapsId("ingredient_id")
    private Ingredient ingredient;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("recipe_id")
    private Recipe recipe;

    // назва середньої колонки
    @Column
    private double quantity;

    public Quantity(Nutrient nutrient, Ingredient ingredient, double quantity, Recipe recipe) {
        this.nutrient = nutrient;
        this.ingredient = ingredient;
        this.quantity = quantity;
        this.id_ = new QuantityId(ingredient.getId(), nutrient.getId(), recipe.getId());
    }
}
