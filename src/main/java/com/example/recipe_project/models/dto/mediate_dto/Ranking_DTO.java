package com.example.recipe_project.models.dto.mediate_dto;

import com.example.recipe_project.models.entity.entities.Ranking;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class Ranking_DTO {
    private int ranking;

    public Ranking_DTO(Ranking ranking) {
        this.ranking = ranking.getRanking();
    }
}
