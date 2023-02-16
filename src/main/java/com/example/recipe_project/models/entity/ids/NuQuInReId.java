package com.example.recipe_project.models.entity.ids;

import lombok.*;

import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class NuQuInReId implements Serializable {
    protected int recipe_id;
    protected int nutrient_id;

//    @Override
//    public boolean equals(Object o) {
//        if (this == o) return true;
//        if (!(o instanceof NuQuInReId)) return false;
//        NuQuInReId that = (NuQuInReId) o;
//        return getRecipe_id() == that.getRecipe_id() && getNutrient_id() == that.getNutrient_id();
//    }
//
//    @Override
//    public int hashCode() {
//        return Objects.hash(getRecipe_id(), getNutrient_id());
//    }
}
