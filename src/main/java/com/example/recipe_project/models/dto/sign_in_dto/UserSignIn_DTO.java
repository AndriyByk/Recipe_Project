package com.example.recipe_project.models.dto.sign_in_dto;

import com.example.recipe_project.models.entity.categories.Role;
import lombok.*;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserSignIn_DTO {
    private String password;
    private String username;
}
