package com.example.recipe_project.models.entity.entities;

import com.example.recipe_project.models.entity.ids.RankId;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@Getter
@Setter
@Table(name = "ranks")
public class Rank {
    @EmbeddedId
    private RankId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("recipe_id")
    private Recipe recipe;

    @Column
    private int rank;

    public Rank(User user, Recipe recipe, int rank) {
        this.user = user;
        this.recipe = recipe;
        this.rank = rank;
        this.id = new RankId(user.getId(), recipe.getId());
    }
}
