package com.example.recipe_project.models.entity.entities;

import com.example.recipe_project.models.entity.categories.IngredientCategory;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
@Getter
@Setter
@AllArgsConstructor
@Table(name = "ingredients")

public class Ingredient {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private int id;
    private String name;
    private String about;

    @ManyToOne
    private IngredientCategory ingredientCategory;

    @OneToMany(mappedBy = "ingredient", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Weight> weights = new ArrayList<>();

    @OneToMany(mappedBy = "ingredient", cascade = CascadeType.ALL)
    private List<Quantity> quantities = new ArrayList<>();

    public Ingredient(String name, String about, IngredientCategory ingredientCategory, List<Quantity> quantities) {
        this.name = name;
        this.about = about;
        this.ingredientCategory = ingredientCategory;
        this.quantities = quantities;
    }
}
