package com.example.recipe_project.models.entity.entities;

import com.example.recipe_project.models.entity.ids.CreatedRecipeId;
import com.example.recipe_project.models.entity.ids.FavoriteRecipeId;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@Getter
@Setter
@Table(name = "created_recipes")
public class CreatedRecipe {
    @EmbeddedId
    private CreatedRecipeId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("recipe_id")
    private Recipe recipe;

    public CreatedRecipe(User user, Recipe recipe) {
        this.id = new CreatedRecipeId(recipe.getId(), user.getId());
        this.user = user;
        this.recipe = recipe;
    }
}
