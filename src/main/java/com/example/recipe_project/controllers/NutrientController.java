package com.example.recipe_project.controllers;

import com.example.recipe_project.dao.INutrientDAO;
import com.example.recipe_project.models.entity.Nutrient;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/nutrients")
@AllArgsConstructor
public class NutrientController {
    private INutrientDAO nutrientDAO;

    @GetMapping("")
    public ResponseEntity<List<Nutrient>> findAllNutrients() {
        return new ResponseEntity<>(nutrientDAO.findAll(), HttpStatus.OK);
    }
    @GetMapping("/{id}")
    public ResponseEntity<Nutrient> findNutrientById(@PathVariable int id) {
        Nutrient nutrient = nutrientDAO.findById(id).orElse(new Nutrient());
        if (nutrient.getId() == 0) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(nutrient, HttpStatus.OK);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Nutrient> updateNutrient(@PathVariable int id, @RequestBody Nutrient nutrient) {
        Nutrient nutrientFromDB = nutrientDAO.findById(id).get();
        if (nutrient.getName() != null)
            nutrientFromDB.setName(nutrient.getName());
        if (nutrient.getCategory() != null)
            nutrientFromDB.setCategory(nutrient.getCategory());
        if (nutrient.getAbout() != null)
            nutrientFromDB.setAbout(nutrient.getAbout());
        if (nutrient.getName() == null &&
                nutrient.getCategory() == null &&
                nutrient.getAbout() == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } else {
            nutrientDAO.save(nutrientFromDB);
            return new ResponseEntity<>(nutrientFromDB, HttpStatus.OK);
        }
    }

    @PostMapping("")
    public ResponseEntity<List<Nutrient>> saveNutrient(@RequestBody Nutrient nutrient) {
        if (nutrient != null) {
            nutrientDAO.save(nutrient);
            return new ResponseEntity<>(nutrientDAO.findAll(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(nutrientDAO.findAll(), HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<List<Nutrient>> deleteNutrient(@PathVariable int id) {
        if (id != 0) {
            nutrientDAO.deleteById(id);
            return new ResponseEntity<>(nutrientDAO.findAll(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(nutrientDAO.findAll(), HttpStatus.BAD_REQUEST);
        }
    }
}
