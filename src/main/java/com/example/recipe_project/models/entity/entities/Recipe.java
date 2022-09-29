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
    @Column(unique = true)
    private String title;
    private String description;
    private double rating;
    private String dateOfCreation;
    @ManyToOne
    private RecipeCategory category;

    @OneToMany(cascade = CascadeType.ALL,
            mappedBy = "recipe",
            orphanRemoval = true)
    private List<Weight> weights;

    @ManyToOne(cascade = CascadeType.ALL)
    private User author;

    @OneToMany(cascade = CascadeType.ALL,
            mappedBy = "recipe",
            orphanRemoval = true)
    private List<FavoriteRecipe> favoriteRecipes;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "recipe")
    private List<Rank> ranks;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "recipe", orphanRemoval = true)
    private List<NutrientQuantityInRecipe> nutrientQuantities;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "recipe", orphanRemoval = true)
    private List<NutrientQuantityInRecipePer100Gramm> nutrientQuantitiesPer100Gram;


//    public Recipe(String image, String title, String description, RecipeCategory recipeCategory, List<Weight> weights) {
//        this.image = image;
//        this.title = title;
//        this.description = description;
////        this.rating = rating;
//        this.recipeCategory = recipeCategory;
//        this.weights = weights;
//    }

//    public Recipe(String image, String title, String description, RecipeCategory recipeCategory, List<Weight> weights, User user, List<Rank> ranks) {
//        this.image = image;
//        this.title = title;
//        this.description = description;
////        this.rating = rating;
//        this.recipeCategory = recipeCategory;
//        this.weights = weights;
//        this.user = user;
//        this.ranks = ranks;
//    }

    public Recipe(String image, String title, String description, String dateOfCreation, RecipeCategory category, List<Weight> weights, User author, List<NutrientQuantityInRecipe> nutrientQuantities, List<NutrientQuantityInRecipePer100Gramm> nutrientQuantitiesPer100Gram) {
        this.image = image;
        this.title = title;
        this.description = description;
        this.dateOfCreation = dateOfCreation;
        this.category = category;
        this.weights = weights;
        // ЗРОБЛЕНО! юзера треба скомпонувати з фронтом - має входити той юзер, який зараз авторизований
        this.author = author;

        // при створенні рецепта, рейтингів ще не має
//        this.ranks = ranks;
        this.nutrientQuantities = nutrientQuantities;
        this.nutrientQuantitiesPer100Gram = nutrientQuantitiesPer100Gram;
    }

//    public Recipe(String image, String title, String description, RecipeCategory category, List<Weight> weights, User author, List<User> users, List<NutrientQuantityInRecipe> nutrientQuantities) {
//        this.image = image;
//        this.title = title;
//        this.description = description;
//        this.category = category;
//        this.weights = weights;
//        // ЗРОБЛЕНО! юзера треба скомпонувати з фронтом - має входити той юзер, який зараз авторизований
//        this.author = author;
////        this.userFavorite = users;
//        // при створенні рецепта, рейтингів ще не має
////        this.ranks = ranks;
//        this.nutrientQuantities = nutrientQuantities;
//    }
}