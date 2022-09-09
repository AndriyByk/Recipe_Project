package com.example.recipe_project.models.entity.entities;

import com.example.recipe_project.models.entity.ids.WeightId;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@Getter
@Setter
@Table(name = "weights")
public class Weight {
    @EmbeddedId
    private WeightId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("ingredient_id")
    private Ingredient ingredient;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("recipe_id")
    private Recipe recipe;

    @Column
    private int weight;

    public Weight(Ingredient ingredient, Recipe recipe, int weight) {
        this.ingredient = ingredient;
        this.recipe = recipe;
        this.weight = weight;
        this.id = new WeightId(recipe.getId(), ingredient.getId());
    }
}
