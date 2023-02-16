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
public class UserNormId implements Serializable {
    private int user_id;
    private int nutrient_id;
}
