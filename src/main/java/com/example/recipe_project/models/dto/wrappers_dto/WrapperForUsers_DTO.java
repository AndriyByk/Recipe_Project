package com.example.recipe_project.models.dto.wrappers_dto;

import com.example.recipe_project.models.dto.entities_dto.User_DTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@Getter
@Setter
@AllArgsConstructor
public class WrapperForUsers_DTO {
    private long totalElements;
    private List<User_DTO> users;
    private int totalPages;
    private int currentPage;
}
