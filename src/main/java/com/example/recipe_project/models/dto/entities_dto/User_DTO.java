package com.example.recipe_project.models.dto.entities_dto;

import com.example.recipe_project.models.dto.categories_dto.ActivityType_DTO;
import com.example.recipe_project.models.dto.categories_dto.Gender_DTO;
import com.example.recipe_project.models.dto.mediate_dto.UserNorm_DTO;
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
public class User_DTO {
    private int id;
    private String username;
    private String password;
    // фотка для відображення на фронті
    private String avatar;
    private String email;
    private int weight;
    private int height;
    private String dayOfBirth;
    private Gender_DTO genderDto;
    private ActivityType_DTO activityTypeDto;
    private String name;
    private String lastName;
    private String dateOfRegistration;
//    private List<Recipe_DTO> favoriteRecipes;
//    private List<Recipe_DTO> createdRecipes;

    private List<Integer> favoriteRecipes;
    private List<Integer> createdRecipes;
    private List<UserNorm_DTO> userNorms;


    public User_DTO(User user) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.password = user.getPassword();
        this.avatar = user.getAvatar();
        this.email = user.getEmail();
        this.weight = user.getWeight();
        this.height = user.getHeight();
        this.dayOfBirth = user.getDayOfBirth();
        this.genderDto = new Gender_DTO(user.getGender());
        this.activityTypeDto = new ActivityType_DTO(user.getActivityType());
        this.name = user.getName();
        this.lastName = user.getLastName();
        this.dateOfRegistration = user.getDateOfRegistration();
        this.createdRecipes = user.getCreatedRecipes().stream().map(Recipe::getId).collect(Collectors.toList());
        this.favoriteRecipes = user.getFavoriteRecipes().stream().map(favoriteRecipe -> favoriteRecipe.getRecipe()
                .getId()).collect(Collectors.toList());
//        this.favoriteRecipes = user
//                .getFavoriteRecipes()
//                .stream()
//                .map(favoriteRecipe -> new Recipe_DTO(
//                        favoriteRecipe.getRecipe()
//                )).collect(Collectors.toList());
//        this.createdRecipes = user
//                .getCreatedRecipes()
//                .stream()
//                .map(Recipe_DTO::new)
//                .collect(Collectors.toList());
        this.userNorms = user.getNorms()
                .stream()
                .map(userNorm -> new UserNorm_DTO(userNorm.getId().getNutrient_id(), userNorm.getNutrient().getEngName(), userNorm.getNutrient().getUkrName(), userNorm.getNutrient().getUnit(), userNorm.getQuantity()))
                .collect(Collectors.toList());
    }
}
