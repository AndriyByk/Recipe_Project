package com.example.recipe_project.models.dto.sign_in_dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserSignIn_DTO {
    private String password;
    private String username;
//    private List<Role> roles;
}
