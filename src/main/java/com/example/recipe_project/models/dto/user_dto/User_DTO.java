package com.example.recipe_project.models.dto.user_dto;

import com.example.recipe_project.models.entity.User;
import com.example.recipe_project.models.entity.enums.ActivityType;
import com.example.recipe_project.models.entity.enums.Gender;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
@JsonIgnoreProperties(value = {"hibernateInitializer"})
public class User_DTO {
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

    public User_DTO(User user) {
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
    }
}
