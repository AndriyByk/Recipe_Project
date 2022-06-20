package com.example.recipe_project.models.entity;

import lombok.*;

import javax.persistence.*;
import java.util.List;

@Entity
@NoArgsConstructor
@Getter
@Setter
//@ToString
@AllArgsConstructor
//@JsonIgnoreProperties(value = {"hibernateLazyInitializer"})
public class Recipe {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private int id;
    private String image;
    private String title;
    private String description;
    private String category;
    private double rating;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "recipe")
    @ToString.Exclude
    private List<IngredientWithWeight> ingredientWithWeights;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "recipe")
    private List<Quantity> quantities;

    public Recipe(String image, String title, String description, String category, double rating, List<IngredientWithWeight> ingredientWithWeights, List<Quantity> quantities) {
        this.image = image;
        this.title = title;
        this.description = description;
        this.category = category;
        this.rating = rating;
        this.ingredientWithWeights = ingredientWithWeights;
        this.quantities = quantities;
    }

    public Recipe(String image, String title, String description, String category, double rating, List<IngredientWithWeight> ingredientWithWeights) {
        this.image = image;
        this.title = title;
        this.description = description;
        this.category = category;
        this.rating = rating;
        this.ingredientWithWeights = ingredientWithWeights;
    }

    //    public Recipe(int id, String image, String title, String description, String category, double rating) {
//        this.id = id;
//        this.image = image;
//        this.title = title;
//        this.description = description;
//        this.category = category;
//        this.rating = rating;
//    }

//    public void addIngredient(Ingredient ingredient, int quantity) {
//        Weight weight = new Weight(ingredient,this, quantity);
//        weights.add(weight);
//        ingredient.getWeights().add(weight);
//    }
}