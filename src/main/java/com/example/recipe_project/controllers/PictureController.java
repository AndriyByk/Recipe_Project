package com.example.recipe_project.controllers;

import com.example.recipe_project.services.pictures.PictureService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class PictureController {
    private PictureService pictureService;

    @GetMapping("/avatar/{id}")
    public String getUserAvatar(@PathVariable int id) {
        return pictureService.getUserAvatar(id);
    }
}
