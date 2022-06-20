package com.example.recipe_project.models.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class IdBetweenRecipeAndIngredient implements Serializable {
    private int recipe_id;
    private int ingredient_id;
}
