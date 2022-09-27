package com.example.recipe_project.models.entity.raw;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
@AllArgsConstructor
public class RawUpdatedUser {
    private String email;
    private int weight;
    private int height;
    private String dayOfBirth;
    private String name;
    private String lastName;
    private int activityTypeId;
    private int genderId;
}
