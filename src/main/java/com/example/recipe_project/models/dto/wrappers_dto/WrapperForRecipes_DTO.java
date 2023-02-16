package com.example.recipe_project.models.dto.wrappers_dto;

import com.example.recipe_project.models.dto.entities_dto.Recipe_DTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@Getter
@Setter
@AllArgsConstructor
public class WrapperForRecipes_DTO {
    private long totalRecipes;
    private List<Recipe_DTO> recipes;
    private int totalPages;
    private int currentPage;

}
