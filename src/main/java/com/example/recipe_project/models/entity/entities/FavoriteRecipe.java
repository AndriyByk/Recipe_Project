package com.example.recipe_project.models.entity.entities;

import com.example.recipe_project.models.entity.ids.FavoriteRecipeId;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@Getter
@Setter
@Table(name="favorite_recipes")
public class FavoriteRecipe {
    @EmbeddedId
    private FavoriteRecipeId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("recipe_id")
    private Recipe recipe;

    public FavoriteRecipe(User user, Recipe recipe) {
        this.id = new FavoriteRecipeId(recipe.getId(), user.getId());
        this.user = user;
        this.recipe = recipe;
    }
}
