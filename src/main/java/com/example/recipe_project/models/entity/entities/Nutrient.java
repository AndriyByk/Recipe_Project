package com.example.recipe_project.models.entity.entities;

import com.example.recipe_project.models.entity.categories.NutrientCategory;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
@Getter
@Setter
@AllArgsConstructor
@Table(name = "nutrients")
public class Nutrient {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private int id;
    private String name;
    private String about;

//    public Nutrient(int id, String name, String about, List<Quantity> quantities, NutrientCategory nutrientCategory, List<NutrientQuantityInRecipe> nutrientQuantities) {
//        this.id = id;
//        this.name = name;
//        this.about = about;
//        this.quantities = quantities;
//        this.nutrientCategory = nutrientCategory;
//        this.nutrientQuantities = nutrientQuantities;
//    }

    @OneToMany(mappedBy = "nutrient", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Quantity> quantities = new ArrayList<>();

    @ManyToOne
    private NutrientCategory nutrientCategory;

    @OneToMany(mappedBy = "nutrient", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<NutrientQuantityInRecipe> nutrientQuantities;

    @OneToMany(mappedBy = "nutrient", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<NutrientQuantityInRecipePer100Gramm> nutrientQuantitiesPer100Gram;

    @OneToMany(mappedBy = "nutrient", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserNorm> norms = new ArrayList<>();
}
