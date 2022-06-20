package com.example.recipe_project.models.entity;

import com.example.recipe_project.models.entity.enums.ActivityType;
import com.example.recipe_project.models.entity.enums.Gender;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@NoArgsConstructor
@Getter
@Setter
@AllArgsConstructor
public class User {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private int id;

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

    @OneToMany(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    private List<Recipe> recipes;
}
