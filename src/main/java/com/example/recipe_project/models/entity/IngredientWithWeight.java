package com.example.recipe_project.models.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class IngredientWithWeight {
    @EmbeddedId
    private IdBetweenRecipeAndIngredient id;

    @ManyToOne(fetch = FetchType.EAGER)
    @MapsId("ingredient_id")
    private Ingredient ingredient;

    @ManyToOne(fetch = FetchType.EAGER)
    @MapsId("recipe_id")
    private Recipe recipe;

    @Column
    private int weight;

    public IngredientWithWeight(Ingredient ingredient, Recipe recipe, int weight) {
        this.ingredient = ingredient;
        this.recipe = recipe;
        this.weight = weight;
        this.id = new IdBetweenRecipeAndIngredient(recipe.getId(), ingredient.getId());
    }
}
