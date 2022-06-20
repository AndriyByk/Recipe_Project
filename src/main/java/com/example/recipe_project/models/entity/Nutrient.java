package com.example.recipe_project.models.entity;

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
public class Nutrient {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private int id;
    private String name;
    private String category;
    private String about;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private List<NutrientWithQuantity> nutrientWithQuantities = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Quantity> quantities = new ArrayList<>();
}
