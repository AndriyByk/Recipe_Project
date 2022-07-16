package com.example.recipe_project.models.entity.raw;

import lombok.*;

@NoArgsConstructor
@Getter
@Setter
@AllArgsConstructor
public class RawUser {
    private String username;
    private String password;
//    private String avatar;
    private String email;
    private int weight;
    private int height;
    private String dayOfBirth;
    private String name;
    private String lastName;
    private String dateOfRegistration;

    private int activityTypeId;
    private int genderId;
}
