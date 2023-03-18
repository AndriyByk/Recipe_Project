package com.example.recipe_project.controllers;

import com.example.recipe_project.models.dto.entities_dto.Recipe_DTO;
import com.example.recipe_project.models.entity.raw.RawComment;
import com.example.recipe_project.services.entities.CommentService;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/comments")
@AllArgsConstructor
public class CommentController {
    private CommentService commentService;

    @PostMapping("")
    public ResponseEntity<Recipe_DTO> saveComment(
            @RequestBody String comment,
            @RequestParam(required = false) int userId,
            @RequestParam(required = false) int recipeId,
            @RequestParam(required = false) String date
    ) throws JsonProcessingException {
        return commentService.saveComment(comment, userId, recipeId, date);
    }

    @DeleteMapping("")
    public ResponseEntity<Recipe_DTO> deleteComment(
            @RequestParam(required = false) int commentId,
            @RequestParam(required = false) int recipeId
    ) {
        return commentService.deleteComment(commentId, recipeId);
    }

}
