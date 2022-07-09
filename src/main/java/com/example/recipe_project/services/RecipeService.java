package com.example.recipe_project.services;

import com.example.recipe_project.dao.*;
import com.example.recipe_project.dao.categories_dao.IRecipeCategoryDAO;
import com.example.recipe_project.models.dto.categories_dto.RecipeCategory_DTO;
import com.example.recipe_project.models.dto.entities_dto.Recipe_DTO;
import com.example.recipe_project.models.entity.raw.RawRecipe;
import com.example.recipe_project.models.entity.entities.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class RecipeService {
    private IRecipeDAO recipeDAO;
    private IIngredientDAO ingredientDAO;
    private IRecipeCategoryDAO recipeCategoryDAO;
    private IQuantityDAO quantityDAO;

    // GET All recipes
    public ResponseEntity<List<Recipe_DTO>> findAllRecipes(int pageNumber, int pageSize) {
        List<Recipe> allRecipes = recipeDAO.findAll(PageRequest.of(pageNumber, pageSize)).getContent();
        List<Recipe_DTO> allRecipe_DTOS = allRecipes
                .stream().map(Recipe_DTO::new)
                .collect(Collectors.toList());
        return new ResponseEntity<>(allRecipe_DTOS, HttpStatus.OK);
    }

    // GET 1 recipe
    public ResponseEntity<Recipe_DTO> findRecipeById(@PathVariable int id) {
        Recipe recipe = recipeDAO.findById(id).orElse(new Recipe());
        if (recipe.getId() == 0) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(new Recipe_DTO(recipe), HttpStatus.OK);
    }

    // PATCH
    public ResponseEntity<Recipe_DTO> updateRecipe(int id, Recipe recipe) {
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
        if (recipe.getRecipeCategory() != null)
            recipeFromDB.setRecipeCategory(recipe.getRecipeCategory());
//        if (recipe.getRating() != 0)
//            recipeFromDB.setRating(recipe.getRating());
        if (recipe.getWeights() != null && recipe.getWeights().size() != 0)
            recipeFromDB.setWeights(recipe.getWeights());
        if (recipe.getTitle() == null &&
                recipe.getImage() == null &&
                recipe.getDescription() == null &&
                recipe.getWeights() == null &&
                recipe.getRecipeCategory() == null) {
//               && recipe.getRating() == 0
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } else {
            recipeDAO.save(recipeFromDB);
            return new ResponseEntity<>(new Recipe_DTO(recipeFromDB), HttpStatus.OK);
        }
    }

    // POST
    public ResponseEntity<List<Recipe_DTO>> saveRecipe(
            String recipe,
            MultipartFile picture,
            int pageNumber,
            int pageSize
    ) throws IOException {
        if (recipe != null) {
//            ObjectMapper objectMapper = new ObjectMapper();
//            objectMapper.rea
            RawRecipe rawRecipe = new ObjectMapper().readValue(recipe, RawRecipe.class);

            // збереження картинки
            String path = System.getProperty("user.home") + File.separator + "Pictures" + File.separator;
            picture.transferTo(new File(path + picture.getOriginalFilename()));

            // пусті листи для:
            // 1) кількості нутрієнтів в рецепті
            // 2) для ваги
            List<NutrientQuantityInRecipe> nutrientQuantities = new ArrayList<>();
            List<Weight> weights = new ArrayList<>();

            // РЕЦЕПТ для бази
            Recipe recipeForDB = new Recipe(
                    rawRecipe.getImage(),
                    rawRecipe.getTitle(),
                    rawRecipe.getDescription(),
//                    recipeWithRawIngredients.getRating(),
                    recipeCategoryDAO.findById(rawRecipe.getRecipeCategoryId()).get(),
                    weights,
                    nutrientQuantities);

            // допоміжна мапа для зручності (для нутрієнтів в рецепті)
            Map<Nutrient, Double> quantities = new HashMap<>();
            // з запиту:
            rawRecipe.getRawIngredientWithWeights().forEach(ingredientWithWeight -> {

                // інгредієнт
                Ingredient ingredient = ingredientDAO.findById(ingredientWithWeight.getId()).get();
                // його вага
                int weight = ingredientWithWeight.getWeight();

                // обрахунок нутрієнтів в рецепті
                quantityDAO.findAll().forEach(quantity -> {
                    Nutrient nutrient = quantity.getNutrient();
                    double quantity_ = quantity.getQuantity() * weight / 100;
                    if (quantities.containsKey(nutrient)) {
                        quantities.replace(nutrient, quantities.get(nutrient) + quantity_);
                    } else {
                        quantities.put(nutrient, quantity_);
                    }
                });

                // зберегти ВАГУ у табличку з вагою
                recipeForDB.getWeights().add(new Weight(ingredient, recipeForDB, weight));
            });

            // покласти інфо про нутрієнти в рецепті в РЕЦЕПТ
            quantities.keySet().forEach(nutrient -> recipeForDB.getNutrientQuantities().add(new NutrientQuantityInRecipe(nutrient, recipeForDB, quantities.get(nutrient))));

            // зберегти РЕЦЕПТ
            recipeDAO.save(recipeForDB);


            return new ResponseEntity<>(recipeDAO.findAll(PageRequest.of(pageNumber, pageSize)).getContent().stream()
                    .map(Recipe_DTO::new)
                    .collect(Collectors.toList()), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(recipeDAO.findAll(PageRequest.of(pageNumber, pageSize)).getContent().stream()
                    .map(Recipe_DTO::new)
                    .collect(Collectors.toList()), HttpStatus.BAD_REQUEST);
        }
    }

    // DELETE
    public ResponseEntity<List<Recipe_DTO>> deleteRecipe(
            int id,
            int pageNumber,
            int pageSize
    ) {
        if (recipeDAO.findAll().stream().anyMatch(recipeDAO -> recipeDAO.getId() == id)) {
            recipeDAO.deleteById(id);
            return new ResponseEntity<>(recipeDAO
                    .findAll(PageRequest.of(pageNumber, pageSize)).getContent()
                    .stream()
                    .map(Recipe_DTO::new).collect(Collectors.toList()), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(recipeDAO
                    .findAll(PageRequest.of(pageNumber, pageSize)).getContent()
                    .stream()
                    .map(Recipe_DTO::new)
                    .collect(Collectors.toList()), HttpStatus.BAD_REQUEST);
        }
    }

    /////////////////////////////////////////////////

    public ResponseEntity<List<RecipeCategory_DTO>> findAllRecipeCategories() {
        return new ResponseEntity<>(recipeCategoryDAO.findAll()
                .stream()
                .map(RecipeCategory_DTO::new)
                .collect(Collectors.toList()), HttpStatus.OK);
    }

    /////////////////////////////////////

    public ResponseEntity<List<Recipe_DTO>> findRecipesByCategory(
            int id,
            int pageNumber,
            int pageSize
    ) {
        int from = pageSize * pageNumber;
        int to = from + pageSize;
        return new ResponseEntity<>(recipeCategoryDAO
                .findById(id)
                .get()
                .getRecipes().subList(from, to)
                .stream()
                .map(Recipe_DTO::new)
                .collect(Collectors.toList()), HttpStatus.OK);
    }
}