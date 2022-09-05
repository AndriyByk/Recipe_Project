package com.example.recipe_project.controllers;

import com.example.recipe_project.models.dto.categories_dto.NutrientCategory_DTO;
import com.example.recipe_project.models.dto.entities_dto.Nutrient_DTO;
import com.example.recipe_project.models.entity.entities.Nutrient;
import com.example.recipe_project.services.entities.NutrientService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/nutrients")
@AllArgsConstructor
public class NutrientController {
    private NutrientService nutrientService;

    @GetMapping("")
    public ResponseEntity<List<Nutrient_DTO>> findAllNutrients(
            @RequestParam(required = false) int pageNumber,
            @RequestParam(required = false) int pageSize
    ) {
        return nutrientService.findAllNutrients(pageNumber, pageSize);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Nutrient_DTO> findNutrientById(@PathVariable int id) {
        return nutrientService.findNutrientById(id);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Nutrient_DTO> updateNutrient(@PathVariable int id, @RequestBody Nutrient nutrient) {
        return nutrientService.updateNutrient(id, nutrient);
    }

    @PostMapping("")
    public ResponseEntity<List<Nutrient_DTO>> saveNutrient(
            @RequestBody Nutrient nutrient,
            @RequestParam(required = false) int pageNumber,
            @RequestParam(required = false) int pageSize
    ) {
        return nutrientService.saveNutrient(nutrient, pageNumber, pageSize);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<List<Nutrient_DTO>> deleteNutrient(
            @PathVariable int id,
            @RequestParam(required = false) int pageNumber,
            @RequestParam(required = false) int pageSize
    ) {
        return nutrientService.deleteNutrient(id, pageNumber, pageSize);
    }

    ///////////////////////////

    @GetMapping("/categories")
    public ResponseEntity<List<NutrientCategory_DTO>> findAllNutrientCategories() {
        return nutrientService.findAllNutrientCategories();
    }

    ///////////////////////////

    @GetMapping("/categories/{id}")
    public ResponseEntity<List<Nutrient_DTO>> findNutrientsByCategory(
            @PathVariable int id,
            @RequestParam(required = false) int pageNumber,
            @RequestParam(required = false) int pageSize
    ) {
        return nutrientService.findNutrientsByCategory(id, pageNumber, pageSize);
    }

}
