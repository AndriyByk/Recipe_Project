package com.example.recipe_project.controllers;

import com.example.recipe_project.dao.IIngredientDAO;
import com.example.recipe_project.dao.IRecipeDAO;
import com.example.recipe_project.models.dto.recipe_dto.RecipeWithIngredientAndNutrient_DTO;
import com.example.recipe_project.models.dto.recipe_dto.RecipeWithRawIngredients;
import com.example.recipe_project.models.entity.Recipe;
import com.example.recipe_project.services.RecipeService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/recipes")
@AllArgsConstructor
public class RecipeController {
    private IRecipeDAO recipeDAO;
    private IIngredientDAO ingredientDAO;
    private RecipeService recipeService;

//    @GetMapping("")
//    public ResponseEntity<List<Recipe>> findAllRecipes() {
//        return new ResponseEntity<>(recipeDAO.findAll(), HttpStatus.OK);
//    }
//
//    @GetMapping("/{id}")
//    public ResponseEntity<Recipe> findRecipeById(@PathVariable int id) {
//        Recipe recipe = recipeDAO.findById(id).orElse(new Recipe());
//        if (recipe.getId() == 0) {
//            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
//        }
//        return new ResponseEntity<>(recipe, HttpStatus.OK);
//    }
//
//    @PatchMapping("/{id}")
//    public ResponseEntity<Recipe> updateRecipe(@PathVariable int id, @RequestBody Recipe recipe) {
//        Recipe recipeFromDB = recipeDAO.findById(id).get();
//        if (recipe.getTitle() != null)
//            recipeFromDB.setTitle(recipe.getTitle());
//        if (recipe.getImage() != null)
//            recipeFromDB.setImage(recipe.getImage());
//        if (recipe.getDescription() != null)
//            recipeFromDB.setDescription(recipe.getDescription());
//        if (recipe.getCategory() != null)
//            recipeFromDB.setCategory(recipe.getCategory());
//        if (recipe.getRating() != 0)
//            recipeFromDB.setRating(recipe.getRating());
//        if (recipe.getTitle() == null &&
//                recipe.getImage() == null &&
//                recipe.getDescription() == null &&
//                recipe.getCategory() == null &&
//                recipe.getRating() == 0) {
//            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
//        } else {
//            recipeDAO.save(recipeFromDB);
//            return new ResponseEntity<>(recipeFromDB, HttpStatus.OK);
//        }
//    }
//
//    @PostMapping("")
//    public ResponseEntity<List<Recipe>> saveRecipe(@RequestBody Recipe recipe) {
//        if (recipe != null) {
//            recipeDAO.save(recipe);
//            return new ResponseEntity<>(recipeDAO.findAll(), HttpStatus.OK);
//        } else {
//            return new ResponseEntity<>(recipeDAO.findAll(), HttpStatus.BAD_REQUEST);
//        }
//    }
//
//    @DeleteMapping("/{id}")
//    public ResponseEntity<List<Recipe>> deleteRecipe(@PathVariable int id) {
//        if (id != 0) {
//            recipeDAO.deleteById(id);
//            return new ResponseEntity<>(recipeDAO.findAll(), HttpStatus.OK);
//        } else {
//            return new ResponseEntity<>(recipeDAO.findAll(), HttpStatus.BAD_REQUEST);
//        }
//    }
    //////////////////////////////////////////////

    @GetMapping("")
    public ResponseEntity<List<RecipeWithIngredientAndNutrient_DTO>> findAllRecipes() {
        List<Recipe> allRecipes = recipeDAO.findAll();
        List<RecipeWithIngredientAndNutrient_DTO> allRecipeWithIngredientAndNutrient_DTOS = allRecipes
                .stream().map(RecipeWithIngredientAndNutrient_DTO::new)
                .collect(Collectors.toList());
        return new ResponseEntity<>(allRecipeWithIngredientAndNutrient_DTOS, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<RecipeWithIngredientAndNutrient_DTO> findRecipeById(@PathVariable int id) {
        Recipe recipe = recipeDAO.findById(id).orElse(new Recipe());
        if (recipe.getId() == 0) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(new RecipeWithIngredientAndNutrient_DTO(recipe), HttpStatus.OK);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<RecipeWithIngredientAndNutrient_DTO> updateRecipe(@PathVariable int id, @RequestBody Recipe recipe) {
        if (recipeDAO.findAll().stream().allMatch(recipeDAO -> recipeDAO.getId() != id)) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        Recipe recipeFromDB = recipeDAO.findById(id).get();
        if (recipe.getTitle() != null)
            recipeFromDB.setTitle(recipe.getTitle());
        if (recipe.getImage() != null)
            recipeFromDB.setImage(recipe.getImage());
        if (recipe.getDescription() != null)
            recipeFromDB.setDescription(recipe.getDescription());
        if (recipe.getCategory() != null)
            recipeFromDB.setCategory(recipe.getCategory());
        if (recipe.getRating() != 0)
            recipeFromDB.setRating(recipe.getRating());
        if (recipe.getTitle() == null &&
                recipe.getImage() == null &&
                recipe.getDescription() == null &&
                recipe.getCategory() == null &&
                recipe.getRating() == 0) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } else {
            recipeDAO.save(recipeFromDB);
            return new ResponseEntity<>(new RecipeWithIngredientAndNutrient_DTO(recipeFromDB), HttpStatus.OK);
        }
    }

    @PostMapping("")
    public ResponseEntity<List<RecipeWithIngredientAndNutrient_DTO>> saveRecipe(@RequestBody RecipeWithRawIngredients recipeWithRawIngredients) {
        if (recipeWithRawIngredients != null) {
            recipeService.saveRecipe(recipeWithRawIngredients);
            return new ResponseEntity<>(recipeDAO.findAll().stream()
                            .map(RecipeWithIngredientAndNutrient_DTO::new)
                            .collect(Collectors.toList()), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(recipeDAO.findAll().stream()
                            .map(RecipeWithIngredientAndNutrient_DTO::new)
                            .collect(Collectors.toList()), HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<List<RecipeWithIngredientAndNutrient_DTO>> deleteRecipe(@PathVariable int id) {
        if (recipeDAO.findAll().stream().anyMatch(recipeDAO -> recipeDAO.getId() == id)) {
            recipeDAO.deleteById(id);
            return new ResponseEntity<>(recipeDAO
                    .findAll()
                    .stream()
                    .map(RecipeWithIngredientAndNutrient_DTO::new).collect(Collectors.toList()), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(recipeDAO
                    .findAll()
                    .stream()
                    .map(RecipeWithIngredientAndNutrient_DTO::new).collect(Collectors.toList()), HttpStatus.BAD_REQUEST);
        }
    }
}
