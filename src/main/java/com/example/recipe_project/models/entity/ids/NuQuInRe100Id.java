package com.example.recipe_project.models.entity.ids;

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
public class NuQuInRe100Id implements Serializable {
    protected int recipe_id;
    protected int nutrient_id;
}
