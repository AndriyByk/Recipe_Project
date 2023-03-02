package com.example.recipe_project.models.entity.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@Getter
@Setter
@AllArgsConstructor
@Table(name = "comments")
public class Comment {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private int id;
    private String message;

    @ManyToOne
    private Recipe recipe;
    @ManyToOne
    private User author;

    public Comment(String message, Recipe recipe, User author) {
        this.message = message;
        this.recipe = recipe;
        this.author = author;
    }
}
