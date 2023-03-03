package com.example.recipe_project.services.entities;

import com.example.recipe_project.dao.entities_dao.ICommentDAO;
import com.example.recipe_project.dao.entities_dao.IRecipeDAO;
import com.example.recipe_project.dao.entities_dao.IUserDAO;
import com.example.recipe_project.models.dto.entities_dto.Recipe_DTO;
import com.example.recipe_project.models.entity.entities.Comment;
import com.example.recipe_project.models.entity.entities.Recipe;
import com.example.recipe_project.models.entity.entities.User;
import com.example.recipe_project.models.entity.raw.RawComment;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CommentService {
    private ICommentDAO commentDAO;
    private IUserDAO userDAO;
    private IRecipeDAO recipeDAO;

    public ResponseEntity<Recipe_DTO> saveComment(String commentRaw, int userId, int recipeId, String date) throws JsonProcessingException {
        RawComment rawComment = new ObjectMapper().readValue(commentRaw, RawComment.class);
        commentDAO.save(new Comment(rawComment.getComment(), recipeDAO.findById(recipeId).get(), userDAO.findById(userId).get(), date));
        return new ResponseEntity<>(new Recipe_DTO(recipeDAO.findById(recipeId).get()), HttpStatus.OK);
    }
}
