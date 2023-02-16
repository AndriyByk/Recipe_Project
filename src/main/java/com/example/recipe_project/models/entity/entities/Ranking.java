package com.example.recipe_project.models.entity.entities;

import com.example.recipe_project.models.entity.ids.RankingId;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@Getter
@Setter
@Table(name = "rankings")
public class Ranking {
    @EmbeddedId
    private RankingId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("recipe_id")
    private Recipe recipe;

    @Column
    private int ranking;

    public Ranking(User user, Recipe recipe, int ranking) {
        this.user = user;
        this.recipe = recipe;
        this.ranking = ranking;
        this.id = new RankingId(user.getId(), recipe.getId());
    }
}
