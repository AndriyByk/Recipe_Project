package com.example.recipe_project.models.entity.entities;

import com.example.recipe_project.models.entity.ids.NuQuInReId;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@Getter
@Setter
@Table(name = "nutrient_quantities_in_recipe")
public class NutrientQuantityInRecipe {
    @EmbeddedId
    protected NuQuInReId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("recipe_id")
    private Recipe recipe;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("nutrient_id")
    private Nutrient nutrient;

    @Column
    private double quantity;

    public NutrientQuantityInRecipe(Nutrient nutrient, Recipe recipe, double quantity) {
        this.recipe = recipe;
        this.nutrient = nutrient;
        this.quantity = quantity;
        this.id = new NuQuInReId(recipe.getId(), nutrient.getId());
    }


}
