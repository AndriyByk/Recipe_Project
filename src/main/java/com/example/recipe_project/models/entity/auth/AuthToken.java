package com.example.recipe_project.models.entity.auth;

import com.example.recipe_project.models.entity.entities.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class AuthToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(unique = true)
    private String token;

//    @ManyToOne(
//            cascade = CascadeType.ALL
//            , fetch = FetchType.EAGER
//
//    )
//    private User user;
}
