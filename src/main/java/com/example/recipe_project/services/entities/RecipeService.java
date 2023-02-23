package com.example.recipe_project.services.entities;

import com.example.recipe_project.dao.categories_dao.IRecipeCategoryDAO;
import com.example.recipe_project.dao.entities_dao.IIngredientDAO;
import com.example.recipe_project.dao.entities_dao.IRecipeDAO;
import com.example.recipe_project.dao.entities_dao.IUserDAO;
import com.example.recipe_project.dao.mediate_dao.IQuantityDAO;
import com.example.recipe_project.dao.mediate_dao.IQuantityInRecipePer100GramDAO;
import com.example.recipe_project.dao.mediate_dao.IRankDAO;
import com.example.recipe_project.models.dto.categories_dto.RecipeCategory_DTO;
import com.example.recipe_project.models.dto.entities_dto.Recipe_DTO;
import com.example.recipe_project.models.dto.wrappers_dto.WrapperForRecipes_DTO;
import com.example.recipe_project.models.entity.raw.RawRecipe;
import com.example.recipe_project.models.entity.entities.*;
import com.example.recipe_project.models.entity.raw.RawRecipeWithUserRate;
import com.example.recipe_project.models.entity.raw.RawUpdatedRecipe;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class RecipeService {
    private IRecipeDAO recipeDAO;
    private IIngredientDAO ingredientDAO;
    private IRecipeCategoryDAO recipeCategoryDAO;
    private IQuantityDAO quantityDAO;
    private IUserDAO userDAO;
    private IQuantityInRecipePer100GramDAO quantityDAO100;
    private IRankDAO rankDAO;

    // GET All recipes
    public ResponseEntity<WrapperForRecipes_DTO> findAllRecipes(int pageNumber, int pageSize) {

        PageRequest pageRequest = PageRequest.of(pageNumber, pageSize);
        Page<Recipe> allRequestPages = recipeDAO.findAll(pageRequest);
        System.out.println(pageNumber + "================================");
        long totalRecipes = allRequestPages.getTotalElements();
        System.out.println(totalRecipes + "================================");
        int totalPages = allRequestPages.getTotalPages();
        System.out.println(totalPages + "================================");
        List<Recipe> allPagesContent = allRequestPages.getContent();
        List<Recipe_DTO> allRecipe_DTOS = allPagesContent
                .stream().map(Recipe_DTO::new)
                .collect(Collectors.toList());
        return new ResponseEntity<>(new WrapperForRecipes_DTO(totalRecipes, allRecipe_DTOS, totalPages, pageNumber), HttpStatus.OK);
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
    public ResponseEntity<Recipe_DTO> updateRecipe(int id, String recipe, MultipartFile picture) throws IOException {
        if (recipe != null) {
            RawUpdatedRecipe rawUpdatedRecipe = new ObjectMapper().readValue(recipe, RawUpdatedRecipe.class);
            Recipe recipeFromDB = recipeDAO.findById(id).get();

            if (picture != null) {
                // збереження картинки
                String path = System.getProperty("user.home") + File.separator
                        + "IdeaProjects" + File.separator
                        + "Recipe_Project" + File.separator
                        + "src" + File.separator
                        + "main" + File.separator
                        + "java" + File.separator
                        + "com" + File.separator
                        + "example" + File.separator
                        + "recipe_project" + File.separator
                        + "pictures" + File.separator
                        + "recipes" + File.separator;

                String pathOfRecipeDir = path + recipeFromDB.getDateOfCreation();
                new File(pathOfRecipeDir + File.separator + recipeFromDB.getImage()).delete();
                recipeFromDB.setImage(picture.getOriginalFilename());
                picture.transferTo(new File(pathOfRecipeDir + File.separator + picture.getOriginalFilename()));
            }

            if (recipeDAO.findAll().stream().allMatch(recipeDAO -> recipeDAO.getId() != id)) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
//        назву вирішив не оновлювати (див. інфу на фронті)
//        if (recipe.getTitle() != null)
//            recipeFromDB.setTitle(recipe.getTitle());
            if (rawUpdatedRecipe.getDescription() != null)
                recipeFromDB.setDescription(rawUpdatedRecipe.getDescription());
            if (rawUpdatedRecipe.getRecipeCategoryId() != 0)
                recipeFromDB.setCategory(recipeCategoryDAO.findById(rawUpdatedRecipe.getRecipeCategoryId()).get());
//        if (recipe.getRating() != 0)
//            recipeFromDB.setRating(recipe.getRating());
            if (rawUpdatedRecipe.getRawIngredientWithWeights() != null && rawUpdatedRecipe.getRawIngredientWithWeights().size() != 0) {
//                всі деталі про кількості нутрієнтів в рецепті слід почистити
                recipeFromDB.getNutrientQuantities().clear();
//                а також слід почистити вагу інгредієнтів
                recipeFromDB.getWeights().clear();
                // допоміжна мапа для зручності (для нутрієнтів в рецепті)
                Map<Nutrient, Double> quantities = new HashMap<>();
                // з запиту:
                rawUpdatedRecipe.getRawIngredientWithWeights().forEach(ingredientWithWeight -> {
                    // якщо з фронта приходять "пусті" інгредієнти
                    if (ingredientWithWeight.getId() != 0) {
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
                        recipeFromDB.getWeights().add(new Weight(ingredient, recipeFromDB, weight));
                    }
                });

                // покласти інфо про нутрієнти в рецепті в РЕЦЕПТ
                quantities.keySet().forEach(nutrient -> recipeFromDB.getNutrientQuantities().add(new NutrientQuantityInRecipe(nutrient, recipeFromDB, quantities.get(nutrient))));
            }
            if (
//                recipe.getTitle() == null &&
                    rawUpdatedRecipe.getDescription() == null &&
                            rawUpdatedRecipe.getRawIngredientWithWeights() == null &&
                            rawUpdatedRecipe.getRawIngredientWithWeights().size() != 0 &&
                            rawUpdatedRecipe.getRecipeCategoryId() == 0) {
//               && recipe.getRating() == 0
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            } else {
                recipeDAO.save(recipeFromDB);
                return new ResponseEntity<>(new Recipe_DTO(recipeFromDB), HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    // POST
    public ResponseEntity<List<Recipe_DTO>> saveRecipe(String recipe, MultipartFile picture, String username) throws IOException {
        if (recipe != null) {
            RawRecipe rawRecipe = new ObjectMapper().readValue(recipe, RawRecipe.class);

            // збереження картинки
            String path = System.getProperty("user.home") + File.separator
                    + "IdeaProjects" + File.separator
                    + "Recipe_Project" + File.separator
                    + "src" + File.separator
                    + "main" + File.separator
                    + "java" + File.separator
                    + "com" + File.separator
                    + "example" + File.separator
                    + "recipe_project" + File.separator
                    + "pictures" + File.separator
                    + "recipes" + File.separator;

            // rawRecipe.getDateOfCreation(): format = dd-MM-yyyy_ss-mm-HH
            String pathOfRecipeDir = path + rawRecipe.getDateOfCreation();

            if (new File(pathOfRecipeDir).mkdir()) {
                picture.transferTo(new File(pathOfRecipeDir + File.separator + picture.getOriginalFilename()));
            }

            // пусті листи для:
            // 1) для кількості нутрієнтів в рецепті
            // 2) для ваги
            List<NutrientQuantityInRecipe> nutrientQuantities = new ArrayList<>();
            List<Weight> weights = new ArrayList<>();
            List<NutrientQuantityInRecipePer100Gramm> nutrientQuantitiesPer100Gram = new ArrayList<>();
            // зберегти рецепт в ЮЗЕРА
            User user = userDAO.findByUsername(username);

            // РЕЦЕПТ для бази
            Recipe recipeForDB = new Recipe(
                    picture.getOriginalFilename(),
                    rawRecipe.getTitle(),
                    rawRecipe.getDescription(),
                    rawRecipe.getDateOfCreation(),
                    0.,
//                    recipeWithRawIngredients.getRating(),
                    recipeCategoryDAO.findById(rawRecipe.getRecipeCategoryId()).get(),
                    weights,
                    user,
                    nutrientQuantities,
                    nutrientQuantitiesPer100Gram);
            // вага всього рецепту
            AtomicInteger recipeWeight = new AtomicInteger();
            // допоміжна мапа для зручності (для нутрієнтів в рецепті)
            Map<Nutrient, Double> quantities = new HashMap<>();
            Map<Nutrient, Double> quantitiesPer100 = new HashMap<>();
            // з запиту:
            rawRecipe.getRawIngredientWithWeights().forEach(ingredientWithWeight -> {

                if (ingredientWithWeight.getId() != 0) {
                    // інгредієнт
                    Ingredient ingredient = ingredientDAO.findById(ingredientWithWeight.getId()).get();

                    // його вага
                    int weight = ingredientWithWeight.getWeight();

                    // обрахунок всієї маси страви
                    recipeWeight.set(recipeWeight.get() + weight);

                    // обрахунок нутрієнтів в рецепті по кожному інгредієнту
                    quantityDAO.findByIngredient(ingredient).forEach(

//                    quantityDAO.findAll().forEach(
                            // конкретний нутрієнт з кількістю
                            quantity -> {
                                // нутрієнт, який рахуємо
                                Nutrient nutrient = quantity.getNutrient();
                                // кількість нутрієнта у всій страві   -- кількість нутрієнта в 100 грам інгредієнта * вага інгредієнта в рецепті в кілограмах
                                double quantity_ = quantity.getQuantity() * weight / 100;

                                if (quantities.containsKey(nutrient)) {
                                    // попередня кількість нутрієнта в страві
                                    Double aDouble = quantities.get(nutrient);
                                    // сума попередньої кількості нутрієнта + кількість нутрієнта в цьому інгредієнті
                                    double v = aDouble + quantity_;
                                    // заокруглення до 0.000
                                    double value = Math.ceil(v * 1000) / 1000;

                                    quantities.replace(nutrient, value);
                                } else {
                                    quantities.put(nutrient, (Math.ceil(quantity_ * 1000) / 1000));
                                }

                                // наповнити мапу[100] ключами нутрієнтів
                                if (!quantitiesPer100.containsKey(nutrient)) {
                                    quantitiesPer100.put(nutrient, 0.);
                                }
                            });

                    // зберегти ВАГУ у табличку з вагою
                    recipeForDB.getWeights().add(new Weight(ingredient, recipeForDB, weight));
                }
            });

//            заповнити мапу[100] значеннями
            quantities.keySet().forEach(nutrient -> {
                double quantity = quantities.get(nutrient);
                double quantityPer100 = quantity * 100 / recipeWeight.get();
                quantitiesPer100.replace(nutrient, (Math.ceil(quantityPer100 * 1000) / 1000));
            });

            // покласти інфо про нутрієнти в рецепті в РЕЦЕПТ
            quantities.keySet().forEach(nutrient -> {
                recipeForDB.getNutrientQuantities().add(new NutrientQuantityInRecipe(nutrient, recipeForDB, quantities.get(nutrient)));
                recipeForDB.getNutrientQuantitiesPer100Gram().add(new NutrientQuantityInRecipePer100Gramm(recipeForDB, nutrient, quantitiesPer100.get(nutrient)));
            });

            // зберегти РЕЦЕПТ
            recipeDAO.save(recipeForDB);

//            Recipe recipeByTitle = recipeDAO.findByTitle(recipeForDB.getTitle());
//            user.getCreatedRecipes().add(recipeByTitle);
//            userDAO.save(user);
            ///////////////////
//            recipeDAO.save(recipeForDB);
//
//            User user = userDAO.findByUsername(username);
//            Recipe recipeByTitle = recipeDAO.findByTitle(recipeForDB.getTitle());
//
//            user.getCreatedRecipes().add(recipeByTitle);
//            recipeByTitle.setUser(user);
//
//            recipeDAO.save(recipeByTitle);
//            userDAO.save(user);
            ///////////////

            return new ResponseEntity<>(recipeDAO.findAll().stream()
                    .map(Recipe_DTO::new)
                    .collect(Collectors.toList()), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(recipeDAO.findAll().stream()
                    .map(Recipe_DTO::new)
                    .collect(Collectors.toList()), HttpStatus.BAD_REQUEST);
        }
    }

    // DELETE
    public ResponseEntity<List<Recipe_DTO>> deleteRecipe(int id, int pageNumber, int pageSize) {
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

    // All recipes` categories
    public ResponseEntity<List<RecipeCategory_DTO>> findAllRecipeCategories() {
        return new ResponseEntity<>(recipeCategoryDAO.findAll()
                .stream()
                .map(RecipeCategory_DTO::new)
                .collect(Collectors.toList()), HttpStatus.OK);
    }

    // Recipes by category (не використовується в такому вигляді на фронті)

//    public ResponseEntity<List<Recipe_DTO>> findRecipesByCategory(int id, int pageNumber, int pageSize) {
//
//
//        int from = pageSize * pageNumber;
//        int to = from + pageSize;
//        long allRecipes = recipeCategoryDAO.findById(id).get().getRecipes().stream().count();
//
//        List<Recipe> recipes = recipeCategoryDAO
//                .findById(id)
//                .get()
//                .getRecipes();
//        return new ResponseEntity<>(recipes.subList(from, to)
//                .stream()
//                .map(Recipe_DTO::new)
//                .collect(Collectors.toList()), HttpStatus.OK);
//    }

    // Recipes by recipeCategoryId & title (or part of it)
//    public ResponseEntity<WrapperForRecipes_DTO> findFilteredRecipes(Integer recipeCategoryId, String title, int pageNumber, int pageSize) {
//
//        int from = pageSize * pageNumber;
//        int to = from + pageSize;
//
//        if (title != null) {
//            if (recipeCategoryId != null) {
//                List<Recipe> allRecipes = recipeDAO.findAllByTitleContainingAndCategory(title, recipeCategoryDAO.findById(recipeCategoryId).get());
//                long numberOfAllRecipes = allRecipes.stream().count();
//                int totalPages = (int) (numberOfAllRecipes / pageSize) + 1;
//                if (to >= numberOfAllRecipes) {
//                    to = (int) numberOfAllRecipes;
//                }
//                List<Recipe_DTO> chosenRecipes = allRecipes.subList(from, to).stream().map(Recipe_DTO::new).collect(Collectors.toList());
//                return new ResponseEntity<>(new WrapperForRecipes_DTO(
//                        numberOfAllRecipes,
//                        chosenRecipes,
//                        totalPages,
//                        pageNumber), HttpStatus.OK);
//            } else {
//                List<Recipe> allRecipes = recipeDAO.findAllByTitleContaining(title);
//                long numberOfAllRecipes = allRecipes.stream().count();
//                int totalPages = (int) (numberOfAllRecipes / pageSize) + 1;
//                if (to >= numberOfAllRecipes) {
//                    to = (int) numberOfAllRecipes;
//                }
//                List<Recipe_DTO> chosenRecipes = allRecipes.subList(from, to).stream().map(Recipe_DTO::new).collect(Collectors.toList());
//                return new ResponseEntity<>(new WrapperForRecipes_DTO(
//                        numberOfAllRecipes,
//                        chosenRecipes,
//                        totalPages,
//                        pageNumber), HttpStatus.OK);
//            }
//        } else {
//            if (recipeCategoryId != null) {
//                List<Recipe> allRecipes = recipeDAO.findAllByCategory(recipeCategoryDAO.findById(recipeCategoryId).get());
//                long numberOfAllRecipes = allRecipes.stream().count();
//                int totalPages = (int) (numberOfAllRecipes / pageSize) + 1;
//                if (to >= numberOfAllRecipes) {
//                    to = (int) numberOfAllRecipes;
//                }
//                List<Recipe_DTO> chosenRecipes = allRecipes.subList(from, to).stream().map(Recipe_DTO::new).collect(Collectors.toList());
//                return new ResponseEntity<>(new WrapperForRecipes_DTO(
//                        numberOfAllRecipes,
//                        chosenRecipes,
//                        totalPages,
//                        pageNumber), HttpStatus.OK);
//            } else {
//                return null;
//            }
//        }
//    }

    // Recipes by nutrientId - descending
//    public ResponseEntity<WrapperForRecipes_DTO> findByNutrient(int nutrientId, int pageNumber, int pageSize) {
//
//        List<NutrientQuantityInRecipePer100Gramm> rawAllRecipes = quantityDAO100.findAllByNutrientIdOrderByQuantityDesc(nutrientId);
//        long numberOfAllRecipes = rawAllRecipes.stream().count();
//
//        int totalPages = (int) (numberOfAllRecipes / pageSize) + 1;
//
//        int from = pageSize * pageNumber;
//        int to = from + pageSize;
//
//        if (to >= numberOfAllRecipes) {
//            to = (int) numberOfAllRecipes;
//        }
//        List<NutrientQuantityInRecipePer100Gramm> rawChosenRecipesPerPage = rawAllRecipes.subList(from, to);
//        List<Recipe_DTO> chosenRecipes = rawChosenRecipesPerPage.stream().map(nutrientQuantity -> new Recipe_DTO(nutrientQuantity.getRecipe())).collect(Collectors.toList());
//        return new ResponseEntity<>(new WrapperForRecipes_DTO(
//                numberOfAllRecipes,
//                chosenRecipes,
//                totalPages,
//                pageNumber), HttpStatus.OK);
//    }

    public ResponseEntity<Recipe_DTO> rateRecipe(String rank) throws JsonProcessingException {

        RawRecipeWithUserRate rateObject = new ObjectMapper().readValue(rank, RawRecipeWithUserRate.class);

//        рецепт, який оцінюється
        Recipe recipe = recipeDAO.findById(rateObject.getRecipeId()).get();
//         юзер, який оцінює
        User user = userDAO.findById(rateObject.getUserId()).get();

        AtomicBoolean b = new AtomicBoolean(false);

// перебираємо всі оцінки
        rankDAO.findAll().forEach(rank1 -> {
//            якщо юзер оцінки є нашим юзером, а рецепт оцінки є рецептом, що оцінюється, то
            if (rank1.getRecipe().getId() == recipe.getId()
                    && rank1.getUser().getId() == user.getId()) {
//                0) беремо стару оцінку
                int oldRanking = rank1.getRanking();
//                1) рахуємо загальну кількість оцінок даного рецепта
                int amount = rankDAO.findAllByRecipe(recipe).size();
                System.out.println(amount);
                AtomicInteger sumAll = new AtomicInteger();
                rankDAO.findAllByRecipe(recipe).forEach(ranking -> sumAll.set(sumAll.get() + ranking.getRanking()));
                sumAll.set(sumAll.get() - oldRanking + rateObject.getRate());
//                2)
                recipe.setRating(sumAll.intValue() / (double) amount);
                recipeDAO.save(recipe);
                rank1.setRanking(rateObject.getRate());
                rankDAO.save(rank1);
//                3)
                System.out.println("rate was changed");
                b.set(true);

            }
        });

        if (!b.get()) {
//            1)
            int amount = rankDAO.findAllByRecipe(recipe).size() + 1;
            AtomicInteger sumAll = new AtomicInteger();
            rankDAO.findAllByRecipe(recipe).forEach(ranking -> sumAll.set(sumAll.get() + ranking.getRanking()));
            sumAll.set(sumAll.intValue() + rateObject.getRate());
//            2)
            recipe.setRating(sumAll.intValue() / (double) amount);
            recipeDAO.save(recipe);
            rankDAO.save(new Ranking(user, recipe, rateObject.getRate()));
//            3)
            System.out.println("!!! rate was created");
            System.out.println(recipe.getRating());
        }
//        List<Rank> ranks = recipe.getRanks();
//        System.out.println("1");
//        Rank rank1 = new Rank(user, recipe, rateObject.getRate());
//        if (!ranks.contains(rank1)) {
//            rankDAO.save(rank1);
//            System.out.println("2");
////            ranks.add(new Rank(user, recipe, rateObject.getRate()));
////            user.setRanks(ranks);
////            recipe.setRanks(ranks);
//        }
//        System.out.println("3");
////        recipe.setRanks(ranks);
////        rankDAO.save()
////        userDAO.save(user);
        return new ResponseEntity<>(new Recipe_DTO(recipe), HttpStatus.OK);
    }

    public ResponseEntity<WrapperForRecipes_DTO> findAndSort(
            int recipeCategoryId, int nutrientId, String title, int pageSize, int pageNumber) {

        System.out.println("nutrientId === " + nutrientId);
        System.out.println("recipeCategoryId === " + recipeCategoryId);
        System.out.println("title === " + title);

        System.out.println("pageNumber === " + pageNumber);
        System.out.println("pageSize === " + pageSize);

        int from = pageSize * pageNumber;
        int to = from + pageSize;


        List<Recipe_DTO> chosenRecipes = new ArrayList<>();
        long numberOfAllRecipes;
        int totalPages;
        List<NutrientQuantityInRecipePer100Gramm> chosenRaw = new ArrayList<>();
        if (!title.equals("undefined") && !title.equals("")) {
            if (recipeCategoryId != 0) {
                List<Recipe> allByTitleAndCategory = recipeDAO
                        .findAllByTitleContainingAndCategory(title, recipeCategoryDAO.findById(recipeCategoryId).get());
                numberOfAllRecipes = allByTitleAndCategory.stream().count();
                totalPages = (int) Math.ceil((double)numberOfAllRecipes / pageSize);
                if (to >= numberOfAllRecipes) {
                    to = (int) numberOfAllRecipes;
                }
                if (nutrientId != 0) {
                    allByTitleAndCategory.forEach(recipe -> chosenRaw.add(quantityDAO100.findByRecipeAndNutrientId(recipe, nutrientId)));
                    chosenRaw.sort((o1, o2) -> (int) (o2.getQuantity() - o1.getQuantity()));
                    List<Recipe_DTO> allRecipesByTitleAndCategorySortedByQuantity = chosenRaw.stream().map(raw -> new Recipe_DTO(raw.getRecipe())).collect(Collectors.toList());
                    chosenRecipes.addAll(allRecipesByTitleAndCategorySortedByQuantity.subList(from, to));
                } else {
                    List<Recipe_DTO> allRecipesByTitleAndCategory = allByTitleAndCategory.stream().map(Recipe_DTO::new).collect(Collectors.toList());
                    chosenRecipes.addAll(allRecipesByTitleAndCategory.subList(from, to));
                }
            } else {
                List<Recipe> allByTitle = recipeDAO.findAllByTitleContaining(title);
                numberOfAllRecipes = allByTitle.stream().count();
                totalPages = (int) Math.ceil((double)numberOfAllRecipes / pageSize);
                if (to >= numberOfAllRecipes) {
                    to = (int) numberOfAllRecipes;
                }
                if (nutrientId != 0) {
                    allByTitle.forEach(recipe -> chosenRaw.add(quantityDAO100.findByRecipeAndNutrientId(recipe, nutrientId)));
                    chosenRaw.sort((o1, o2) -> (int) (o2.getQuantity() - o1.getQuantity()));
                    List<Recipe_DTO> allRecipesByTitleSortedByQuantity = chosenRaw.stream().map(raw -> new Recipe_DTO(raw.getRecipe())).collect(Collectors.toList());
                    chosenRecipes.addAll(allRecipesByTitleSortedByQuantity.subList(from, to));
                } else {
                    List<Recipe_DTO> allRecipesByTitle = allByTitle.stream().map(Recipe_DTO::new).collect(Collectors.toList());
                    chosenRecipes.addAll(allRecipesByTitle.subList(from, to));
                }
            }
        } else {
            if (recipeCategoryId != 0) {
                List<Recipe> allByCategory = recipeDAO.findAllByCategory(recipeCategoryDAO.findById(recipeCategoryId).get());
                numberOfAllRecipes = allByCategory.stream().count();
                totalPages = (int) Math.ceil ((double)numberOfAllRecipes / pageSize);
                if (to >= numberOfAllRecipes) {
                    to = (int) numberOfAllRecipes;
                }
                if (nutrientId != 0) {
                    allByCategory.forEach(recipe -> chosenRaw.add(quantityDAO100.findByRecipeAndNutrientId(recipe, nutrientId)));
                    chosenRaw.sort((o1, o2) -> (int) (o2.getQuantity() - o1.getQuantity()));
                    List<Recipe_DTO> allRecipesByCategorySortedByQuantity = chosenRaw.stream().map(raw -> new Recipe_DTO(raw.getRecipe())).collect(Collectors.toList());
                    chosenRecipes.addAll(allRecipesByCategorySortedByQuantity.subList(from, to));
                } else {
                    List<Recipe_DTO> allRecipesByCategory = allByCategory.stream().map(Recipe_DTO::new).collect(Collectors.toList());
                    chosenRecipes.addAll(allRecipesByCategory.subList(from, to));
                }
            } else {
                if (nutrientId != 0) {
                    List<NutrientQuantityInRecipePer100Gramm> rawAllRecipes = quantityDAO100.findAllByNutrientIdOrderByQuantityDesc(nutrientId);
                    numberOfAllRecipes = rawAllRecipes.stream().count();
                    totalPages = (int) Math.ceil ((double)numberOfAllRecipes / pageSize);
                    if (to >= numberOfAllRecipes) {
                        to = (int) numberOfAllRecipes;
                    }
                    List<NutrientQuantityInRecipePer100Gramm> rawChosenRecipesPerPage = rawAllRecipes.subList(from, to);
                    chosenRecipes.addAll(rawChosenRecipesPerPage.stream().map(nutrientQuantity -> new Recipe_DTO(nutrientQuantity.getRecipe())).collect(Collectors.toList()));
                } else {
                    PageRequest pageRequest = PageRequest.of(pageNumber, pageSize);
                    Page<Recipe> allRequestPages = recipeDAO.findAll(pageRequest);
                    numberOfAllRecipes = allRequestPages.getTotalElements();
                    totalPages = allRequestPages.getTotalPages();
                    List<Recipe> allPagesContent = allRequestPages.getContent();
                    chosenRecipes.addAll(allPagesContent
                            .stream().map(Recipe_DTO::new)
                            .collect(Collectors.toList()));
                }
            }
        }
        return new ResponseEntity<>(new WrapperForRecipes_DTO(
                numberOfAllRecipes,
                chosenRecipes,
                totalPages,
                pageNumber
        ), HttpStatus.OK);
    }
}