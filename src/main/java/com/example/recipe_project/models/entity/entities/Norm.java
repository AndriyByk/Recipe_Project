package com.example.recipe_project.models.entity.entities;

import com.example.recipe_project.models.entity.categories.norm.Type;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@Getter
@Setter
@AllArgsConstructor
@Table(name = "norms")

public class Norm {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private int id;

    private double age;
    private String sex;

    @ManyToOne
    private Type type;

    private int weight;
    private double quantity;

    @ManyToOne
    private Nutrient nutrient;
}
