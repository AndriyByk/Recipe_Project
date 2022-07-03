package com.example.recipe_project.models.entity.entities;

import com.example.recipe_project.models.entity.entities.Ingredient;
import com.example.recipe_project.models.entity.entities.Nutrient;
import com.example.recipe_project.models.entity.ids.QuantityId;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@Getter
@Setter
@Table(name = "quantities")
public class Quantity {
    @EmbeddedId
    private QuantityId id;

    @ManyToOne(fetch = FetchType.LAZY)
    // назва колонки таблички, яка створюється
    @MapsId("nutrient_id")
    private Nutrient nutrient;

    @ManyToOne(fetch = FetchType.LAZY)
    // назва колонки таблички, яка створюється
    @MapsId("ingredient_id")
    private Ingredient ingredient;

    // назва середньої колонки
    @Column
    private double quantity;

    public Quantity(Nutrient nutrient, Ingredient ingredient, double quantity) {
        this.nutrient = nutrient;
        this.ingredient = ingredient;
        this.quantity = quantity;
        this.id = new QuantityId(ingredient.getId(), nutrient.getId());
    }
}
