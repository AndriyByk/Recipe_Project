package com.example.recipe_project.models.dto.entities_dto;

import com.example.recipe_project.models.entity.entities.Comment;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class CommentAuthor_DTO {
    private int id;
    private String username;

    public CommentAuthor_DTO(Comment comment) {
        this.id = comment.getAuthor().getId();
        this.username =  comment.getAuthor().getUsername();
    }
}
