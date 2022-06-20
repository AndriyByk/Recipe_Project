package com.example.recipe_project.models.dto.user_dto;

import com.example.recipe_project.models.dto.recipe_dto.RecipeWithIngredientAndNutrient_DTO;
import com.example.recipe_project.models.entity.Recipe;
import com.example.recipe_project.models.entity.User;
import com.example.recipe_project.models.entity.enums.ActivityType;
import com.example.recipe_project.models.entity.enums.Gender;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class UserWithR_DTO {
    private String login;
    private String password;
    private String email;
    private int weight;
    private int height;
    private int age;
    private Gender gender;
    private ActivityType activityType;
    private String name;
    private String lastName;
    private String dateOfRegistration;
    private List<RecipeWithIngredientAndNutrient_DTO> recipes;

    public UserWithR_DTO(User user) {
        this.login = user.getLogin();
        this.password = user.getPassword();
        this.email = user.getEmail();
        this.weight = user.getWeight();
        this.height = user.getHeight();
        this.age = user.getAge();
        this.gender = user.getGender();
        this.activityType = user.getActivityType();
        this.name = user.getName();
        this.lastName = user.getLastName();
        this.dateOfRegistration = user.getDateOfRegistration();
        List<RecipeWithIngredientAndNutrient_DTO> recipeDTOS = new ArrayList<>();
        List<Recipe> recipes = new ArrayList<>(user.getRecipes());
        for (Recipe recipe : recipes) {
            recipeDTOS.add(new RecipeWithIngredientAndNutrient_DTO(recipe));
        }
        this.recipes = recipeDTOS;
    }
}
