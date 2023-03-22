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

//    000000000000000000 AdminMode 00000000000000000000
    @GetMapping("/allRecipes/admin-mode/{pageNumber}")
    public ResponseEntity<WrapperForRecipes_DTO> findAllRecipesInAdminMode(
            @PathVariable int pageNumber,
            @RequestParam(required = false) int pageSize,
            @RequestParam(required = false) String checked
    ) {
        return recipeService.findAllRecipesInAdminMode(pageNumber, pageSize, checked);
    }
//    00000000000000000000000000000000000000000000000

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

//    000000000000000000 AdminMode 00000000000000000000
    @GetMapping("/find-and-sort/admin-mode/{pageNumber}")
    public ResponseEntity<WrapperForRecipes_DTO> findAndSortInAdminMode(
            @RequestParam(required = false) Integer categoryId,
            @RequestParam(required = false) String title,
            @RequestParam(required = false) int pageSize,
            @RequestParam(required = false) int nutrientId,
            @PathVariable int pageNumber
    ) {
        return recipeService.findAndSortInAdminMode(categoryId,nutrientId,title,pageSize,pageNumber);
    }
//    00000000000000000000000000000000000000000000000

    @GetMapping("/created")
    public ResponseEntity<WrapperForRecipes_DTO> getCreated(
            @RequestParam(required = false) int pageSize,
            @RequestParam(required = false) int userId,
            @RequestParam(required = false) int pageNumber
    ) {
        return recipeService.getCreated(pageSize, userId, pageNumber);
    }

    @GetMapping("/created/find-and-sort/{pageNumber}")
    public ResponseEntity<WrapperForRecipes_DTO> getCreatedFilteredAndSorted(
            @RequestParam(required = false) Integer categoryId,
            @RequestParam(required = false) String title,
            @RequestParam(required = false) int pageSize,
            @RequestParam(required = false) int nutrientId,
            @RequestParam(required = false) int userId,
            @PathVariable int pageNumber
    ) {
        return recipeService.getCreatedFilteredAndSorted(categoryId,nutrientId,title,pageSize,userId,pageNumber);
    }

    @GetMapping("/favorite/{pageNumber}")
    public ResponseEntity<WrapperForRecipes_DTO> getFavorite(
            @RequestParam(required = false) int pageSize,
            @RequestParam(required = false) int userId,
            @PathVariable int pageNumber
    ) {
        return recipeService.getFavorite(pageSize, userId, pageNumber);
    }

    @GetMapping("/favorite/find-and-sort/{pageNumber}")
    public ResponseEntity<WrapperForRecipes_DTO> getFavoriteFilteredAndSorted(
            @RequestParam(required = false) Integer categoryId,
            @RequestParam(required = false) String title,
            @RequestParam(required = false) int pageSize,
            @RequestParam(required = false) int nutrientId,
            @RequestParam(required = false) int userId,
            @PathVariable int pageNumber
    ) {
        return recipeService.getFavoriteFilteredAndSorted(categoryId,nutrientId,title,pageSize,userId,pageNumber);
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
    public ResponseEntity<Recipe_DTO> saveRecipe(
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

    @PatchMapping("/change-status")
    public ResponseEntity<Recipe_DTO> changeStatus(
            @RequestBody String body,
            @RequestParam(required = false) String recipeId
    ) {
        return recipeService.changeStatus(recipeId);
    }
}
