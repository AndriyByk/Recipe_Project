package com.example.recipe_project.controllers;

import com.example.recipe_project.models.dto.categories_dto.RecipeCategory_DTO;
import com.example.recipe_project.models.dto.entities_dto.Recipe_DTO;
import com.example.recipe_project.models.dto.wrappers_dto.WrapperForRecipes_DTO;
import com.example.recipe_project.models.entity.entities.Recipe;
import com.example.recipe_project.services.entities.RecipeService;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/recipes")
@AllArgsConstructor
public class RecipeController {
    private RecipeService recipeService;

    @GetMapping("/allRecipes/{pageNumber}")
    public ResponseEntity<WrapperForRecipes_DTO> findAllRecipes(
            @PathVariable int pageNumber,
            @RequestParam(required = false) int pageSize
    ) {
        return recipeService.findAllRecipes(pageNumber, pageSize);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Recipe_DTO> findRecipeById(
            @PathVariable int id
    ) {
        return recipeService.findRecipeById(id);
    }

    @GetMapping("/find-and-sort/{pageNumber}")
    public ResponseEntity<WrapperForRecipes_DTO> findAndSort(
            @RequestParam(required = false) Integer categoryId,
            @RequestParam(required = false) String title,
            @RequestParam(required = false) int pageSize,
            @RequestParam(required = false) int nutrientId,
            @PathVariable int pageNumber
    ) {
        return recipeService.findAndSort(categoryId,nutrientId,title,pageSize,pageNumber);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Recipe_DTO> updateRecipe(
            @PathVariable int id,
            @RequestParam String recipe,
            @RequestParam(required = false) MultipartFile picture
    ) throws IOException {
        return recipeService.updateRecipe(id, recipe, picture);
    }

    @PostMapping("/{username}")
    public ResponseEntity<List<Recipe_DTO>> saveRecipe(
            @RequestParam String recipe,
            @PathVariable String username,
            @RequestParam(required = false) MultipartFile picture
    ) throws IOException {
        return recipeService.saveRecipe(recipe, picture, username);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<List<Recipe_DTO>> deleteRecipe(
            @PathVariable int id,
            @RequestParam(required = false) int pageNumber,
            @RequestParam(required = false) int pageSize
    ) {
        return recipeService.deleteRecipe(id, pageNumber, pageSize);
    }

    @GetMapping("/categories")
    public ResponseEntity<List<RecipeCategory_DTO>> findAllRecipeCategories() {
        return recipeService.findAllRecipeCategories();
    }

    @PatchMapping("/rate")
    public ResponseEntity<Recipe_DTO> rateRecipe(
            @RequestBody String rate
    ) throws JsonProcessingException {
        return recipeService.rateRecipe(rate);
    }
}
