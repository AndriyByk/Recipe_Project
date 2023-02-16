package com.example.recipe_project.models.entity.ids;

import lombok.*;

import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class WeightId implements Serializable {
    private int recipe_id;
    private int ingredient_id;
}
