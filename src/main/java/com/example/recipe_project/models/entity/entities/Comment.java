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
    private String date;
    @ManyToOne
    private Recipe recipe;
    @ManyToOne
    private User author;

    public Comment(String message, Recipe recipe, User author, String date) {
        this.message = message;
        this.recipe = recipe;
        this.author = author;
        this.date = date;
    }
}
