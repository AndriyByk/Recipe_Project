package com.example.recipe_project.controllers;

import com.example.recipe_project.models.dto.categories_dto.IngredientCategory_DTO;
import com.example.recipe_project.models.dto.entities_dto.Ingredient_DTO;
import com.example.recipe_project.models.dto.entities_dto.Recipe_DTO;
import com.example.recipe_project.models.entity.entities.Ingredient;
import com.example.recipe_project.models.entity.raw.RawIngredient;
import com.example.recipe_project.services.IngredientService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/ingredients")
@AllArgsConstructor
public class IngredientController {
    private IngredientService ingredientService;

    @GetMapping("")
    public ResponseEntity<List<Ingredient_DTO>> findAllIngredients(
            @RequestParam(required = false) int pageNumber,
            @RequestParam(required = false) int pageSize
    ) {
        return ingredientService.findAllIngredients(pageNumber, pageSize);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Ingredient_DTO> findIngredientById(@PathVariable int id) {
        return ingredientService.findIngredientById(id);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Ingredient_DTO> updateIngredient(@PathVariable int id, @RequestBody Ingredient ingredient) {
        return ingredientService.updateIngredient(id, ingredient);
    }

    @PostMapping("")
    public ResponseEntity<List<Ingredient_DTO>> saveIngredient (
            @RequestBody RawIngredient rawIngredient,
            @RequestParam(required = false) int pageNumber,
            @RequestParam(required = false) int pageSize
    ) {
        return ingredientService.saveIngredient(rawIngredient, pageNumber, pageSize);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<List<Ingredient_DTO>> deleteIngredient(
            @PathVariable int id,
            @RequestParam(required = false) int pageNumber,
            @RequestParam(required = false) int pageSize
    ) {
       return ingredientService.deleteIngredient(id, pageNumber, pageSize);
    }

    //////////////////

    @GetMapping("/categories")
    public ResponseEntity<List<IngredientCategory_DTO>> findAllIngredientCategories() {
        return ingredientService.findAllIngredientCategories();
    }

    ///////////////////////////

    @GetMapping("/categories/{id}")
    public ResponseEntity<List<Ingredient_DTO>> findIngredientsByCategory(
            @PathVariable int id,
            @RequestParam(required = false) int pageNumber,
            @RequestParam(required = false) int pageSize
    ) {
        return ingredientService.findIngredientsByCategory(id, pageNumber, pageSize);
    }

    //////////////////////////

    @GetMapping("/{ingredientId}/recipes")
    public ResponseEntity<List<Recipe_DTO>> findRecipesByIngredient(
            @PathVariable int ingredientId,
            @RequestParam(required = false) int pageNumber,
            @RequestParam(required = false) int pageSize
    ) {
        return ingredientService.findRecipesByIngredient(ingredientId, pageNumber, pageSize);
    }
}
