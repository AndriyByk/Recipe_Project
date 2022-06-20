package com.example.recipe_project.models.entity;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
@Getter
@Setter
@AllArgsConstructor
//@JsonIgnoreProperties(value = {"hibernateLazyInitializer"})
public class Ingredient {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private int id;
    private String name;
    private String about;
    private String type;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private List<IngredientWithWeight> ingredientWithWeights = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL)
    private List<NutrientWithQuantity> nutrientWithQuantities = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL)
    private List<Quantity> quantities = new ArrayList<>();

    public Ingredient(String name, String about, String type, List<IngredientWithWeight> ingredientWithWeights, List<NutrientWithQuantity> nutrientWithQuantities) {
        this.name = name;
        this.about = about;
        this.type = type;
        this.ingredientWithWeights = ingredientWithWeights;
        this.nutrientWithQuantities = nutrientWithQuantities;
    }
}
