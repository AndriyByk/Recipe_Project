package com.example.recipe_project.models.dto.entities_dto;

import com.example.recipe_project.models.dto.categories_dto.ActivityType_DTO;
import com.example.recipe_project.models.dto.categories_dto.Gender_DTO;
import com.example.recipe_project.models.entity.entities.User;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
@JsonIgnoreProperties(value = {"hibernateInitializer"})
public class User_DTO {
    private int id;
    private String login;
    private String password;
    private String avatar;
    private String email;
    private int weight;
    private int height;
    private int age;
    private Gender_DTO genderDto;
    private ActivityType_DTO activityTypeDto;
    private String name;
    private String lastName;
    private String dateOfRegistration;

    public User_DTO(User user) {
        this.id = user.getId();
        this.login = user.getLogin();
        this.password = user.getPassword();
        this.avatar = user.getAvatar();
        this.email = user.getEmail();
        this.weight = user.getWeight();
        this.height = user.getHeight();
        this.age = user.getAge();
        this.genderDto = new Gender_DTO(user.getGender());
        this.activityTypeDto = new ActivityType_DTO(user.getActivityType());
        this.name = user.getName();
        this.lastName = user.getLastName();
        this.dateOfRegistration = user.getDateOfRegistration();
    }
}
