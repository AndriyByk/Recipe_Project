package com.example.recipe_project.controllers;

import com.example.recipe_project.dao.IIngredientDAO;
import com.example.recipe_project.models.entity.Ingredient;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/ingredients")
@AllArgsConstructor
public class IngredientController {
    private IIngredientDAO ingredientDAO;

    @GetMapping("")
    public ResponseEntity<List<Ingredient>> findAllIngredients() {
        return new ResponseEntity<>(ingredientDAO.findAll(), HttpStatus.OK);
    }
    @GetMapping("/{id}")
    public ResponseEntity<Ingredient> findIngredientById(@PathVariable int id) {
        Ingredient ingredient = ingredientDAO.findById(id).orElse(new Ingredient());
        if (ingredient.getId() == 0) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(ingredient, HttpStatus.OK);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Ingredient> updateIngredient(@PathVariable int id, @RequestBody Ingredient ingredient) {
        Ingredient ingredientFromDB = ingredientDAO.findById(id).get();
        if (ingredient.getName() != null)
            ingredientFromDB.setName(ingredient.getName());
        if (ingredient.getAbout() != null)
            ingredientFromDB.setAbout(ingredient.getAbout());
        if (ingredient.getType() != null)
            ingredientFromDB.setType(ingredient.getType());
        if (ingredient.getName() == null &&
                ingredient.getAbout() == null &&
                ingredient.getType() == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } else {
            ingredientDAO.save(ingredientFromDB);
            return new ResponseEntity<>(ingredientFromDB, HttpStatus.OK);
        }
    }

    @PostMapping("")
    public ResponseEntity<List<Ingredient>> saveIngredient (@RequestBody Ingredient ingredient) {
        if (ingredient != null) {
            ingredientDAO.save(ingredient);
            return new ResponseEntity<>(ingredientDAO.findAll(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(ingredientDAO.findAll(), HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<List<Ingredient>> deleteIngredient(@PathVariable int id) {
        if (id != 0) {
            ingredientDAO.deleteById(id);
            return new ResponseEntity<>(ingredientDAO.findAll(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(ingredientDAO.findAll(), HttpStatus.BAD_REQUEST);
        }
    }
}
