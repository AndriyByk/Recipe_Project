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
    private String date;
    private CommentAuthor_DTO author;

    public Comment_DTO(Comment comment) {
        this.id = comment.getId();
        this.date = comment.getDate();
        this.message = comment.getMessage();
        this.author = new CommentAuthor_DTO(comment);
    }
}
