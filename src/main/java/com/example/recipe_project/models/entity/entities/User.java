package com.example.recipe_project.models.entity.entities;

import com.example.recipe_project.models.entity.categories.ActivityType;

import com.example.recipe_project.models.entity.categories.Gender;
import com.example.recipe_project.models.entity.entities.Rank;
import com.example.recipe_project.models.entity.entities.Recipe;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
@Getter
@Setter
@AllArgsConstructor
@Table(name = "users")
public class User {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private int id;

    private String login;
    private String password;
    private String avatar;
    private String email;
    private int weight;
    private int height;
    private int age;

    @ManyToOne(cascade = CascadeType.ALL)
    private Gender gender;

    @ManyToOne(cascade = CascadeType.ALL)
    private ActivityType activityType;

    private String name;
    private String lastName;
    private String dateOfRegistration;

    // Lists
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinTable(name = "favorite_recipes", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "recipe_id"))
    private List<Recipe> favoriteRecipes;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinTable(name = "created_recipes", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "recipe_id"))
    private List<Recipe> createdRecipes;

    // Rank
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Rank> ranks = new ArrayList<>();

    public User(String login, String password, String avatar, String email, int weight, int height, int age, Gender gender, ActivityType activityType, String name, String lastName, String dateOfRegistration) {
        this.login = login;
        this.password = password;
        this.avatar = avatar;
        this.email = email;
        this.weight = weight;
        this.height = height;
        this.age = age;
        this.gender = gender;
        this.activityType = activityType;
        this.name = name;
        this.lastName = lastName;
        this.dateOfRegistration = dateOfRegistration;
    }

    public User(String login,
                String password,
                String avatar,
                String email,
                int weight,
                int height,
                int age,
                Gender gender,
                ActivityType activityType,
                String name,
                String lastName,
                String dateOfRegistration,
                List<Recipe> favoriteRecipes,
                List<Recipe> createdRecipes,
                List<Rank> ranks) {
        this.login = login;
        this.password = password;
        this.avatar = avatar;
        this.email = email;
        this.weight = weight;
        this.height = height;
        this.age = age;
        this.gender = gender;
        this.activityType = activityType;
        this.name = name;
        this.lastName = lastName;
        this.dateOfRegistration = dateOfRegistration;
        this.favoriteRecipes = favoriteRecipes;
        this.createdRecipes = createdRecipes;
        this.ranks = ranks;
    }

    public User(int id, String login, String password, String avatar, String email, int weight, int height, int age, Gender gender, ActivityType activityType, String name, String lastName, String dateOfRegistration) {
        this.id = id;
        this.login = login;
        this.password = password;
        this.avatar = avatar;
        this.email = email;
        this.weight = weight;
        this.height = height;
        this.age = age;
        this.gender = gender;
        this.activityType = activityType;
        this.name = name;
        this.lastName = lastName;
        this.dateOfRegistration = dateOfRegistration;
    }
}
