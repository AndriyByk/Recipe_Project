package com.example.recipe_project.models.entity.entities;

import com.example.recipe_project.models.entity.categories.RecipeCategory;
import com.example.recipe_project.models.entity.categories.Status;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
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
    private String dateOfCreation;
    private double rating;
    private Status status;
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

    @OneToMany(cascade = CascadeType.ALL,
            mappedBy = "recipe",
            orphanRemoval = true)
    private List<Ranking> rankings = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "recipe", orphanRemoval = true)
    private List<NutrientQuantityInRecipe> nutrientQuantities;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "recipe", orphanRemoval = true)
    private List<NutrientQuantityInRecipePer100Gramm> nutrientQuantitiesPer100Gram;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "recipe", orphanRemoval = true)
    private List<Comment> comments;

    public Recipe(String image, String title, String description, String dateOfCreation, double rating, Status status, RecipeCategory category, List<Weight> weights, User author, List<NutrientQuantityInRecipe> nutrientQuantities, List<NutrientQuantityInRecipePer100Gramm> nutrientQuantitiesPer100Gram, List<Comment> comments) {
        this.image = image;
        this.title = title;
        this.description = description;
        this.dateOfCreation = dateOfCreation;
        this.rating = rating;
        this.status = status;
        this.category = category;
        this.weights = weights;
        this.author = author;
        this.nutrientQuantities = nutrientQuantities;
        this.nutrientQuantitiesPer100Gram = nutrientQuantitiesPer100Gram;
        this.comments = comments;
    }


}