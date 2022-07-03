package com.example.recipe_project.models.entity.ids;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class QuantityId implements Serializable {
    private int ingredient_id;
    private int nutrient_id;

//    @Override
//    public boolean equals(Object o) {
//        if (this == o) return true;
//        if (!(o instanceof QuantityId)) return false;
//        QuantityId that = (QuantityId) o;
//        return getIngredient_id() == that.getIngredient_id() && getNutrient_id() == that.getNutrient_id();
//    }
//
//    @Override
//    public int hashCode() {
//        return Objects.hash(getIngredient_id(), getNutrient_id());
//    }
}
