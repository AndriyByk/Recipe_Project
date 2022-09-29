package com.example.recipe_project.models.entity.entities;

import com.example.recipe_project.models.entity.ids.NuQuInRe100Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@Getter
@Setter
@Table(name = "nutrient_quantities_in_recipe_100")
public class NutrientQuantityInRecipePer100Gramm {
    @EmbeddedId
    protected NuQuInRe100Id id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("recipe_id")
    private Recipe recipe;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("nutrient_id")
    private Nutrient nutrient;

    @Column
    private double quantity;

    public NutrientQuantityInRecipePer100Gramm(Recipe recipe, Nutrient nutrient, double quantity) {
        this.recipe = recipe;
        this.nutrient = nutrient;
        this.quantity = quantity;
        this.id = new NuQuInRe100Id(recipe.getId(), nutrient.getId());
    }
}
