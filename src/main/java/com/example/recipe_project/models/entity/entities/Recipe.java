package com.example.recipe_project.models.entity.entities;

import com.example.recipe_project.models.entity.categories.RecipeCategory;
import lombok.*;

import javax.persistence.*;
import java.util.List;

@Entity
@NoArgsConstructor
@Getter
@Setter
@AllArgsConstructor
@Table(name = "recipes")
public class Recipe {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private int id;
    private String image;
    private String title;
    private String description;
    private double rating;

    @ManyToOne
    private RecipeCategory recipeCategory;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "recipe", orphanRemoval = true)
    private List<Weight> weights;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinTable(name = "created_recipes",
            joinColumns = @JoinColumn(name = "recipe_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private User user;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "recipe")
    private List<Rank> ranks;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "recipe", orphanRemoval = true)
    private List<NutrientQuantityInRecipe> nutrientQuantities;

    public Recipe(String image, String title, String description, RecipeCategory recipeCategory, List<Weight> weights) {
        this.image = image;
        this.title = title;
        this.description = description;
//        this.rating = rating;
        this.recipeCategory = recipeCategory;
        this.weights = weights;
    }

    public Recipe(String image, String title, String description, RecipeCategory recipeCategory, List<Weight> weights, User user, List<Rank> ranks) {
        this.image = image;
        this.title = title;
        this.description = description;
//        this.rating = rating;
        this.recipeCategory = recipeCategory;
        this.weights = weights;
        this.user = user;
        this.ranks = ranks;
    }

    public Recipe(String image, String title, String description, RecipeCategory recipeCategory, List<Weight> weights, List<NutrientQuantityInRecipe> nutrientQuantities) {
        this.image = image;
        this.title = title;
        this.description = description;
        this.recipeCategory = recipeCategory;
        this.weights = weights;
        // юзера треба скомпонувати з фронтом - має входити той юзер, який зараз авторизований
//        this.user = user;
        // при створенні рецепта, рейтингів ще не має
//        this.ranks = ranks;
        this.nutrientQuantities = nutrientQuantities;
    }
}