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
public class QuantityId implements Serializable {
    private static final long serialVersionUID = 2393137907499034508L;
    private int ingredient_id;
    private int nutrient_id;
    private int recipe_id;
}
