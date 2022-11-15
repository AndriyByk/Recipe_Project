package com.example.recipe_project.services.entities;

import com.example.recipe_project.dao.entities_dao.IIngredientDAO;
import com.example.recipe_project.dao.entities_dao.INutrientDAO;
import com.example.recipe_project.dao.categories_dao.IIngredientCategoryDAO;
import com.example.recipe_project.models.dto.categories_dto.IngredientCategory_DTO;
import com.example.recipe_project.models.dto.entities_dto.Ingredient_DTO;
import com.example.recipe_project.models.dto.entities_dto.Recipe_DTO;
import com.example.recipe_project.models.entity.entities.Ingredient;
import com.example.recipe_project.models.entity.entities.Nutrient;
import com.example.recipe_project.models.entity.entities.Quantity;
import com.example.recipe_project.models.entity.raw.RawIngredient;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class IngredientService {
    private IIngredientDAO ingredientDAO;
    private IIngredientCategoryDAO ingredientCategoryDAO;
    private INutrientDAO nutrientDAO;

    // GET All
    public ResponseEntity<List<Ingredient_DTO>> findAllIngredientsWithPages(int pageNumber,
                                                                            int pageSize) {
        return new ResponseEntity<>(ingredientDAO
                .findAll(PageRequest.of(pageNumber, pageSize)).getContent()
                .stream()
                .map(Ingredient_DTO::new)
                .collect(Collectors.toList()), HttpStatus.OK);
    }

    public ResponseEntity<List<Ingredient_DTO>> findAllIngredients() {
        return new ResponseEntity<>(ingredientDAO
                .findAll()
                .stream()
                .map(Ingredient_DTO::new)
                .collect(Collectors.toList()), HttpStatus.OK);
    }

    // GET 1
    public ResponseEntity<Ingredient_DTO> findIngredientById(int id) {
        Ingredient ingredient = ingredientDAO.findById(id).orElse(new Ingredient());
        if (ingredient.getId() == 0) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(new Ingredient_DTO(ingredient), HttpStatus.OK);
    }

    // PATCH
    public ResponseEntity<Ingredient_DTO> updateIngredient(int id, Ingredient ingredient) {
        Ingredient ingredientFromDB = ingredientDAO.findById(id).get();
        if (ingredient.getName() != null)
            ingredientFromDB.setName(ingredient.getName());
        if (ingredient.getAbout() != null)
            ingredientFromDB.setAbout(ingredient.getAbout());
        if (ingredient.getIngredientCategory() != null)
            ingredientFromDB.setIngredientCategory(ingredient.getIngredientCategory());
        if (ingredient.getName() == null &&
                ingredient.getAbout() == null &&
                ingredient.getIngredientCategory() == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } else {
            ingredientDAO.save(ingredientFromDB);
            return new ResponseEntity<>(new Ingredient_DTO(ingredientFromDB), HttpStatus.OK);
        }
    }

    // POST
    public ResponseEntity<List<Ingredient_DTO>> saveIngredient(RawIngredient rawIngredient, int pageNumber,
                                                               int pageSize) {
        if (rawIngredient != null) {
            List<Quantity> quantities = new ArrayList<>();
            Ingredient ingredient = new Ingredient(
                    rawIngredient.getName(),
                    rawIngredient.getAbout(),
                    rawIngredient.getName_ukr(),
                    ingredientCategoryDAO.findById(rawIngredient.getIngredientCategoryId()).get(),
                    quantities
            );
            rawIngredient.getRawNutrients().forEach(rawNutrient -> {
                Nutrient nutrient = nutrientDAO.findById(rawNutrient.getNutrientId()).get();
                double quantity = rawNutrient.getQuantity();
                ingredient.getQuantities().add(new Quantity(nutrient, ingredient, quantity));
            });

            ingredientDAO.save(ingredient);

            return new ResponseEntity<>(ingredientDAO
                    .findAll(PageRequest.of(pageNumber, pageSize)).getContent()
                    .stream().map(Ingredient_DTO::new)
                    .collect(Collectors.toList()), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(ingredientDAO
                    .findAll(PageRequest.of(pageNumber, pageSize)).getContent()
                    .stream().map(Ingredient_DTO::new)
                    .collect(Collectors.toList()), HttpStatus.BAD_REQUEST);
        }
    }

    // DELETE
    public ResponseEntity<List<Ingredient_DTO>> deleteIngredient(int id, int pageNumber,
                                                                 int pageSize) {
        if (id != 0) {
            ingredientDAO.deleteById(id);
            return new ResponseEntity<>(ingredientDAO
                    .findAll(PageRequest.of(pageNumber, pageSize)).getContent()
                    .stream()
                    .map(Ingredient_DTO::new)
                    .collect(Collectors.toList()), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(ingredientDAO
                    .findAll(PageRequest.of(pageNumber, pageSize)).getContent()
                    .stream()
                    .map(Ingredient_DTO::new)
                    .collect(Collectors.toList()), HttpStatus.BAD_REQUEST);
        }
    }

    ////////////////////////////////////////////

    public ResponseEntity<List<IngredientCategory_DTO>> findAllIngredientCategories() {
        return new ResponseEntity<>(ingredientCategoryDAO.findAll()
                .stream().map(IngredientCategory_DTO::new)
                .collect(Collectors.toList()), HttpStatus.OK);
    }

    ////////////////////////////////////////////

    public ResponseEntity<List<Ingredient_DTO>> findIngredientsByCategory(int id, int pageNumber,
                                                                          int pageSize) {
        int from = pageSize * pageNumber;
        int to = from + pageSize;
        return new ResponseEntity<>(ingredientCategoryDAO
                .findById(id)
                .get()
                .getIngredients().subList(from, to)
                .stream()
                .map(Ingredient_DTO::new)
                .collect(Collectors.toList()), HttpStatus.OK);
    }

    ////////////////////////////////////////////

    public ResponseEntity<List<Recipe_DTO>> findRecipesByIngredient(int ingredientId, int pageNumber,
                                                                    int pageSize) {
        int from = pageSize * pageNumber;
        int to = from + pageSize;

        List<Recipe_DTO> recipes = new ArrayList<>();
        ingredientDAO.findById(ingredientId).get().getWeights().subList(from, to).forEach(weight -> recipes.add(new Recipe_DTO(weight.getRecipe())));
        return new ResponseEntity<>(recipes, HttpStatus.OK);
    }

}
