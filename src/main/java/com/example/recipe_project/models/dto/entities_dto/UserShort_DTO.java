package com.example.recipe_project.models.dto.entities_dto;

import com.example.recipe_project.models.entity.entities.Recipe;
import com.example.recipe_project.models.entity.entities.User;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor
@Getter
@Setter
@JsonIgnoreProperties(value = {"hibernateInitializer"})
public class UserShort_DTO {
    private int id;
    private String username;
    private String avatar;
    private String role;
    private String dateOfRegistration;
//    private List<Recipe_DTO> createdRecipes;
    private List<Integer> createdRecipes;

    public UserShort_DTO(User user) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.avatar = user.getAvatar();
        this.role = user.getRoles().get(0).name();
        this.dateOfRegistration = user.getDateOfRegistration();
        this.createdRecipes = user.getCreatedRecipes().stream().map(Recipe::getId).collect(Collectors.toList());

//        this.createdRecipes = user
//                .getCreatedRecipes()
//                .stream()
//                .map(Recipe_DTO::new)
//                .collect(Collectors.toList());
    }
}
