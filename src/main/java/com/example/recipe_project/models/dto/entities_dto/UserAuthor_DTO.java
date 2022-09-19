package com.example.recipe_project.models.dto.entities_dto;

import com.example.recipe_project.models.entity.entities.User;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
@JsonIgnoreProperties(value = {"hibernateInitializer"})

public class UserAuthor_DTO {
    private int id;
    private String username;

    public UserAuthor_DTO(User user) {
        this.id = user.getId();
        this.username = user.getUsername();
    }
}
