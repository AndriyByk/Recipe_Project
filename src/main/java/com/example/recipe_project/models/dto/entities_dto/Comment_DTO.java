package com.example.recipe_project.models.dto.entities_dto;

import com.example.recipe_project.models.entity.entities.Comment;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class Comment_DTO {
    private int id;
    private String message;

    public Comment_DTO(Comment comment) {
        this.id = comment.getId();
        this.message = comment.getMessage();
    }
}
