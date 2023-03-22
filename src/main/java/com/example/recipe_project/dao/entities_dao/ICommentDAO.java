package com.example.recipe_project.dao.entities_dao;

import com.example.recipe_project.models.entity.entities.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ICommentDAO extends JpaRepository<Comment, Integer> {
}
