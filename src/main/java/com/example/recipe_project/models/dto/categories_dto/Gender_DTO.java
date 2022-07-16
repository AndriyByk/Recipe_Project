package com.example.recipe_project.models.dto.categories_dto;

import com.example.recipe_project.models.entity.categories.Gender;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class Gender_DTO {
    private int id;
    private String gender;

    public Gender_DTO(Gender gender) {
        this.id = gender.getId();
        this.gender = gender.getGender();
    }
}
