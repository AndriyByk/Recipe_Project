package com.example.recipe_project.models.entity.entities;

import com.example.recipe_project.models.entity.ids.UserNormId;
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
@Table(name = "user_norms")
public class UserNorm {
    @EmbeddedId
    private UserNormId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("nutrient_id")
    private Nutrient nutrient;

    private double quantity;
}
