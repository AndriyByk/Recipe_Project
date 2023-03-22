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
    private String engName;
    private String ukrName;
    private String about;
    private String unit;

    @ManyToOne
    private NutrientCategory nutrientCategory;

    @OneToMany(mappedBy = "nutrient", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Quantity> quantities = new ArrayList<>();

    @OneToMany(mappedBy = "nutrient", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<NutrientQuantityInRecipe> nutrientQuantities;

    @OneToMany(mappedBy = "nutrient", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<NutrientQuantityInRecipePer100Gramm> nutrientQuantitiesPer100Gram;

    @OneToMany(mappedBy = "nutrient", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserNorm> norms = new ArrayList<>();

    public Nutrient(String engName, String ukrName, String about, String unit, NutrientCategory nutrientCategory, List<Quantity> quantities, List<NutrientQuantityInRecipe> nutrientQuantities, List<NutrientQuantityInRecipePer100Gramm> nutrientQuantitiesPer100Gram, List<UserNorm> norms) {
        this.engName = engName;
        this.ukrName = ukrName;
        this.about = about;
        this.unit = unit;
        this.nutrientCategory = nutrientCategory;
        this.quantities = quantities;
        this.nutrientQuantities = nutrientQuantities;
        this.nutrientQuantitiesPer100Gram = nutrientQuantitiesPer100Gram;
        this.norms = norms;
    }
}
