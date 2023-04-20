package com.example.recipe_project.services.entities;

import com.example.recipe_project.dao.categories_dao.IRecipeCategoryDAO;
import com.example.recipe_project.dao.entities_dao.IIngredientDAO;
import com.example.recipe_project.dao.entities_dao.IRecipeDAO;
import com.example.recipe_project.dao.entities_dao.IUserDAO;
import com.example.recipe_project.dao.mediate_dao.IFavoriteRecipeDAO;
import com.example.recipe_project.dao.mediate_dao.IQuantityDAO;
import com.example.recipe_project.dao.mediate_dao.IQuantityInRecipePer100GramDAO;
import com.example.recipe_project.dao.mediate_dao.IRankDAO;
import com.example.recipe_project.models.dto.categories_dto.RecipeCategory_DTO;
import com.example.recipe_project.models.dto.entities_dto.Recipe_DTO;
import com.example.recipe_project.models.dto.wrappers_dto.WrapperForRecipes_DTO;
import com.example.recipe_project.models.entity.categories.Status;
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
    private IFavoriteRecipeDAO favoriteRecipeDAO;

    // GET All recipes
    public ResponseEntity<WrapperForRecipes_DTO> findAllRecipes(int pageNumber, int pageSize) {
        List<Recipe> allByStatusIn = recipeDAO.findAllByStatusIn(Arrays.asList(Status.CHECKED));
        return getWrapperForRecipes_dtoResponseEntity(pageNumber, pageSize, allByStatusIn);
    }

    public ResponseEntity<WrapperForRecipes_DTO> findAllRecipesInAdminMode(int pageNumber, int pageSize, String checked) {
        if (checked.equals("undefined")) {
            PageRequest pageRequest = PageRequest.of(pageNumber, pageSize);
            Page<Recipe> allRequestPages = recipeDAO.findAll(pageRequest);
            long totalRecipes = allRequestPages.getTotalElements();
            int totalPages = allRequestPages.getTotalPages();
            List<Recipe_DTO> allRecipe_DTOS = getDtosFromRecipes(allRequestPages.getContent());
            return new ResponseEntity<>(new WrapperForRecipes_DTO(totalRecipes, allRecipe_DTOS, totalPages, pageNumber), HttpStatus.OK);
        } else if (checked.equals("false")) {
            List<Recipe> allByStatusIn = recipeDAO.findAllByStatusIn(Arrays.asList(Status.UNCHECKED));
            return getWrapperForRecipes_dtoResponseEntity(pageNumber, pageSize, allByStatusIn);
        } else {
            return findAllRecipes(pageNumber, pageSize);
        }
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
    public ResponseEntity<Recipe_DTO> updateRecipe(int id, String recipe) throws IOException {
        if (recipe != null) {
            RawUpdatedRecipe rawUpdatedRecipe = new ObjectMapper().readValue(recipe, RawUpdatedRecipe.class);
            Recipe recipeFromDB = recipeDAO.findById(id).get();

            // якщо рецепт оновлюється користувачем, його ще має схвалити адмін, тож іде в список до UNCHECKED
            recipeFromDB.setStatus(Status.UNCHECKED);

            // закоментовано для [deploy to heroku. avoiding using pictures]
//            if (picture != null) {
//                // збереження картинки
//                String pathOfRecipeDir = getPathOfRecipeDir(recipeFromDB.getDateOfCreation());
//                new File(pathOfRecipeDir + File.separator + recipeFromDB.getImage()).delete();
//                recipeFromDB.setImage(picture.getOriginalFilename());
//                picture.transferTo(new File(pathOfRecipeDir + File.separator + picture.getOriginalFilename()));
//            }

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
                    rawUpdatedRecipe.getDescription() == null &&
                            rawUpdatedRecipe.getRawIngredientWithWeights() == null &&
                            rawUpdatedRecipe.getRawIngredientWithWeights().size() != 0 &&
                            rawUpdatedRecipe.getRecipeCategoryId() == 0) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            } else {
                recipeDAO.save(recipeFromDB);
                return new ResponseEntity<>(new Recipe_DTO(recipeFromDB), HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    // POST
    public ResponseEntity<Recipe_DTO> saveRecipe(String recipe, String username) throws IOException {
        if (recipe != null) {
            RawRecipe rawRecipe = new ObjectMapper().readValue(recipe, RawRecipe.class);

            // збереження картинки
            // закоментовано для [deploy to heroku. avoiding using pictures]
//            // rawRecipe.getDateOfCreation(): format = dd-MM-yyyy_ss-mm-HH
//            String pathOfRecipeDir = getPathOfRecipeDir(rawRecipe.getDateOfCreation());
//            if (new File(pathOfRecipeDir).mkdir()) {
//                picture.transferTo(new File(pathOfRecipeDir + File.separator + picture.getOriginalFilename()));
//            }

            // пусті листи для:
            // 1) для кількості нутрієнтів в рецепті
            // 2) для ваги
            List<NutrientQuantityInRecipe> nutrientQuantities = new ArrayList<>();
            List<Weight> weights = new ArrayList<>();
            List<NutrientQuantityInRecipePer100Gramm> nutrientQuantitiesPer100Gram = new ArrayList<>();
            List<Comment> comments = new ArrayList<>();
            // зберегти рецепт в ЮЗЕРА
            User user = userDAO.findByUsername(username);

            // РЕЦЕПТ для бази
            Recipe recipeForDB = new Recipe(
//                    picture.getOriginalFilename(),
                    "recipe.jpg",
                    rawRecipe.getTitle(),
                    rawRecipe.getDescription(),
                    rawRecipe.getDateOfCreation(),
                    0.,
                    Status.UNCHECKED,
                    recipeCategoryDAO.findById(rawRecipe.getRecipeCategoryId()).get(),
                    weights,
                    user,
                    nutrientQuantities,
                    nutrientQuantitiesPer100Gram,
                    comments);
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

                            // конкретний нутрієнт з кількістю
                            quantity -> {
                                // нутрієнт, який рахуємо
                                Nutrient nutrient = quantity.getNutrient();
                                // кількість нутрієнта у всій страві == кількість нутрієнта в 100 грам інгредієнта * вага інгредієнта в рецепті в кілограмах
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

            return new ResponseEntity<>(new Recipe_DTO(recipeForDB), HttpStatus.OK);

//            return new ResponseEntity<>(recipeDAO.findAll().stream()
//                    .map(Recipe_DTO::new)
//                    .collect(Collectors.toList()), HttpStatus.OK);
        } else {
//            заглушка
            return new ResponseEntity<>(new Recipe_DTO(), HttpStatus.BAD_REQUEST);
        }
    }

    // DELETE
   public ResponseEntity<String> deleteRecipe(int id) {
//        ще треба добавити видалення фото і папки з фото рецепту як це в UserService
       // з обрізаними картинками - необов'язково
        if (recipeDAO.findAll().stream().anyMatch(recipe -> recipe.getId() == id)) {
            recipeDAO.deleteById(id);
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    // All recipes` categories
    public ResponseEntity<List<RecipeCategory_DTO>> findAllRecipeCategories() {
        return new ResponseEntity<>(recipeCategoryDAO.findAll()
                .stream()
                .map(RecipeCategory_DTO::new)
                .collect(Collectors.toList()), HttpStatus.OK);
    }

    public ResponseEntity<WrapperForRecipes_DTO> findAndSort(Integer recipeCategoryId, int nutrientId, String title, int pageSize, int pageNumber) {
        int from = pageSize * pageNumber;
        int to = from + pageSize;

        List<Recipe> recipes;
        long numberOfAllRecipes;
        int totalPages;
        List<NutrientQuantityInRecipePer100Gramm> chosenRaw = new ArrayList<>();
        List<Recipe_DTO> chosenRecipes = new ArrayList<>();

        if (!title.equals("undefined") && !title.equals("")) {
            if (recipeCategoryId != 0) {
                recipes = recipeDAO.findAllByStatusInAndTitleContainingAndCategory(Collections.singletonList(Status.CHECKED), title, recipeCategoryDAO.findById(recipeCategoryId).get());
            } else {
                recipes = recipeDAO.findAllByStatusInAndTitleContaining(Collections.singletonList(Status.CHECKED), title);
            }
        } else {
            if (recipeCategoryId != 0) {
                recipes = recipeDAO.findAllByStatusInAndCategory(Collections.singletonList(Status.CHECKED), recipeCategoryDAO.findById(recipeCategoryId).get());
            } else {
                recipes = recipeDAO.findAllByStatusIn(Collections.singletonList(Status.CHECKED));
            }
        }
        numberOfAllRecipes = getCount(recipes);
        totalPages = getTotalPages(numberOfAllRecipes, pageSize);
        to = verifyTo(to, numberOfAllRecipes);
        if (nutrientId != 0) {
            choseQuantityInRecipeByNutrient(nutrientId, chosenRaw, recipes);
            sortChosen(chosenRaw);
            chosenRecipes.addAll(getChosenDtosFromNQPer100(from, to, chosenRaw));
        } else {
            chosenRecipes.addAll(getChosenDtosFromRecipes(from, to, recipes));
        }

        return new ResponseEntity<>(new WrapperForRecipes_DTO(
                numberOfAllRecipes,
                chosenRecipes,
                totalPages,
                pageNumber
        ), HttpStatus.OK);
    }

    public ResponseEntity<WrapperForRecipes_DTO> findAndSortInAdminMode(Integer recipeCategoryId, int nutrientId, String title, int pageSize, int pageNumber) {
        int from = pageSize * pageNumber;
        int to = from + pageSize;

        List<Recipe> recipes;
        long numberOfAllRecipes;
        int totalPages;
        List<NutrientQuantityInRecipePer100Gramm> chosenRaw = new ArrayList<>();
        List<Recipe_DTO> chosenRecipes = new ArrayList<>();

        if (!title.equals("undefined") && !title.equals("")) {
            if (recipeCategoryId != 0) {
                recipes = recipeDAO.findAllByTitleContainingAndCategory(title, recipeCategoryDAO.findById(recipeCategoryId).get());
            } else {
                recipes = recipeDAO.findAllByTitleContaining(title);
            }
        } else {
            if (recipeCategoryId != 0) {
                recipes = recipeDAO.findAllByCategory(recipeCategoryDAO.findById(recipeCategoryId).get());
            } else {
                recipes = recipeDAO.findAll();
            }
        }
        numberOfAllRecipes = getCount(recipes);
        totalPages = getTotalPages(numberOfAllRecipes, pageSize);
        to = verifyTo(to, numberOfAllRecipes);
        if (nutrientId != 0) {
            choseQuantityInRecipeByNutrient(nutrientId, chosenRaw, recipes);
            sortChosen(chosenRaw);
            chosenRecipes.addAll(getChosenDtosFromNQPer100(from, to, chosenRaw));
        } else {
            chosenRecipes.addAll(getChosenDtosFromRecipes(from, to, recipes));
        }

        return new ResponseEntity<>(new WrapperForRecipes_DTO(
                numberOfAllRecipes,
                chosenRecipes,
                totalPages,
                pageNumber
        ), HttpStatus.OK);
    }

    public ResponseEntity<WrapperForRecipes_DTO> getCreated(int pageSize, int userId, int pageNumber) {
        User user = userDAO.findById(userId).get();
        int from = pageSize * pageNumber;
        int to = from + pageSize;
        long numberOfAllRecipes = getCount(user.getCreatedRecipes());
        int totalPages = getTotalPages(numberOfAllRecipes, pageSize);
        to = verifyTo(to, numberOfAllRecipes);
        List<Recipe_DTO> chosenRecipes = user.getCreatedRecipes().subList(from, to).stream().map(Recipe_DTO::new)
                .collect(Collectors.toList());

        return new ResponseEntity<>(new WrapperForRecipes_DTO(
                numberOfAllRecipes,
                chosenRecipes,
                totalPages,
                pageNumber
        ), HttpStatus.OK);
    }

    public ResponseEntity<WrapperForRecipes_DTO> getCreatedFilteredAndSorted(Integer recipeCategoryId, int nutrientId, String title, int pageSize, int userId, int pageNumber) {
        User user = userDAO.findById(userId).get();
        int from = pageSize * pageNumber;
        int to = from + pageSize;

        List<Recipe> chosenRecipes;
        long numberOfAllRecipes;
        int totalPages;
        List<NutrientQuantityInRecipePer100Gramm> chosenRaw = new ArrayList<>();

        List<Recipe_DTO> sortedChosenRecipes = new ArrayList<>();

        if (!title.equals("undefined") && !title.equals("")) {
            if (recipeCategoryId != 0) {
                chosenRecipes = recipeDAO.findAllByAuthorAndTitleContainingAndCategory(user, title, recipeCategoryDAO.findById(recipeCategoryId).get());
            } else {
                chosenRecipes = recipeDAO.findAllByAuthorAndTitleContaining(user, title);
            }
        } else {
            if (recipeCategoryId != 0) {
                chosenRecipes = recipeDAO.findAllByAuthorAndCategory(user, recipeCategoryDAO.findById(recipeCategoryId).get());
            } else {
                chosenRecipes = recipeDAO.findAllByAuthor(user);
            }
        }

        numberOfAllRecipes = getCount(chosenRecipes);
        totalPages = getTotalPages(numberOfAllRecipes, pageSize);
        to = verifyTo(to, numberOfAllRecipes);
        if (nutrientId != 0) {
            choseQuantityInRecipeByNutrient(nutrientId, chosenRaw, chosenRecipes);
            sortChosen(chosenRaw);
            sortedChosenRecipes.addAll(getChosenDtosFromNQPer100(from, to, chosenRaw));
        } else {
            sortedChosenRecipes.addAll(getChosenDtosFromRecipes(from, to, chosenRecipes));
        }

        return new ResponseEntity<>(new WrapperForRecipes_DTO(
                numberOfAllRecipes,
                sortedChosenRecipes,
                totalPages,
                pageNumber
        ), HttpStatus.OK);
    }

    public ResponseEntity<WrapperForRecipes_DTO> getFavorite(int pageSize, int userId, int pageNumber) {
        User user = userDAO.findById(userId).get();
        int from = pageSize * pageNumber;
        int to = from + pageSize;
        long numberOfAllRecipes = user.getFavoriteRecipes().stream().count();
        int totalPages = getTotalPages(numberOfAllRecipes, pageSize);
        to = verifyTo(to, numberOfAllRecipes);
        List<Recipe_DTO> chosenRecipes = user.getFavoriteRecipes().subList(from, to).stream().map(favoriteRecipe -> new Recipe_DTO(favoriteRecipe.getRecipe()))
                .collect(Collectors.toList());

        return new ResponseEntity<>(new WrapperForRecipes_DTO(
                numberOfAllRecipes,
                chosenRecipes,
                totalPages,
                pageNumber
        ), HttpStatus.OK);
    }

    public ResponseEntity<WrapperForRecipes_DTO> getFavoriteFilteredAndSorted(Integer recipeCategoryId, int nutrientId, String title, int pageSize, int userId, int pageNumber) {
        User user = userDAO.findById(userId).get();
        List<FavoriteRecipe> allByUser = favoriteRecipeDAO.findAllByUser(user);
        List<Recipe> favoriteRecipes = allByUser.stream().map(FavoriteRecipe::getRecipe).collect(Collectors.toList());

        int from = pageSize * pageNumber;
        int to = from + pageSize;

        List<Recipe> chosenRecipes;
        long numberOfAllRecipes;
        int totalPages;
        List<NutrientQuantityInRecipePer100Gramm> chosenRaw = new ArrayList<>();
        List<Recipe_DTO> sortedChosenFavoriteRecipes = new ArrayList<>();

        if (!title.equals("undefined") && !title.equals("")) {
            if (recipeCategoryId != 0) {
                chosenRecipes = recipeDAO.findAllByTitleContainingAndCategory(title, recipeCategoryDAO.findById(recipeCategoryId).get());
            } else {
                chosenRecipes = recipeDAO.findAllByTitleContaining(title);
            }
        } else {
            if (recipeCategoryId != 0) {
                chosenRecipes = recipeDAO.findAllByCategory(recipeCategoryDAO.findById(recipeCategoryId).get());
            } else {
                chosenRecipes = recipeDAO.findAll();
            }
        }
        List<Recipe> chosenFavoriteRecipes = chosenRecipes.stream().filter(favoriteRecipes::contains).collect(Collectors.toList());

        numberOfAllRecipes = getCount(chosenFavoriteRecipes);
        totalPages = getTotalPages(numberOfAllRecipes, pageSize);
        to = verifyTo(to, numberOfAllRecipes);
        if (nutrientId != 0) {
            choseQuantityInRecipeByNutrient(nutrientId, chosenRaw, chosenRecipes);
            sortChosen(chosenRaw);
            sortedChosenFavoriteRecipes.addAll(getChosenDtosFromNQPer100(from, to, chosenRaw));
        } else {
            sortedChosenFavoriteRecipes.addAll(getChosenDtosFromRecipes(from, to, chosenRecipes));
        }

        return new ResponseEntity<>(new WrapperForRecipes_DTO(
                numberOfAllRecipes,
                sortedChosenFavoriteRecipes,
                totalPages,
                pageNumber
        ), HttpStatus.OK);
    }

    public ResponseEntity<Recipe_DTO> rateRecipe(String rank) throws JsonProcessingException {
        RawRecipeWithUserRate rateObject = new ObjectMapper().readValue(rank, RawRecipeWithUserRate.class);

//        рецепт, який оцінюється
        Recipe recipe = recipeDAO.findById(rateObject.getRecipeId()).get();
//         юзер, який оцінює
        User user = userDAO.findById(rateObject.getUserId()).get();

        AtomicBoolean isRanked = new AtomicBoolean(false);

//      перевіряємо, чи юзер раніше оцінював рецепт
//      перебираємо всі оцінки
        rankDAO.findAll().forEach(rank1 -> {
//            якщо юзер оцінки є нашим юзером, а рецепт оцінки є рецептом, що оцінюється, то
            if (rank1.getRecipe().getId() == recipe.getId()
                    && rank1.getUser().getId() == user.getId()) {
//                0) беремо стару оцінку
                int oldRanking = rank1.getRanking();
//                1) рахуємо загальну кількість оцінок даного рецепта
                int amount = rankDAO.findAllByRecipe(recipe).size();
                AtomicInteger sumAll = new AtomicInteger();
                rankDAO.findAllByRecipe(recipe).forEach(ranking -> sumAll.set(sumAll.get() + ranking.getRanking()));
                sumAll.set(sumAll.get() - oldRanking + rateObject.getRate());
//                2)
                recipe.setRating(sumAll.intValue() / (double) amount);
                recipeDAO.save(recipe);
                rank1.setRanking(rateObject.getRate());
                rankDAO.save(rank1);
//                3) змінюємо статус: оцінений
                isRanked.set(true);
            }
        });

//        якщо юзер ще не оцінював рецепт
        if (!isRanked.get()) {
//            1)
            int amount = rankDAO.findAllByRecipe(recipe).size() + 1;
            AtomicInteger sumAll = new AtomicInteger();
            rankDAO.findAllByRecipe(recipe).forEach(ranking -> sumAll.set(sumAll.get() + ranking.getRanking()));
            sumAll.set(sumAll.intValue() + rateObject.getRate());
//            2)
            recipe.setRating(sumAll.intValue() / (double) amount);
            recipeDAO.save(recipe);
            rankDAO.save(new Ranking(user, recipe, rateObject.getRate()));
        }
        return new ResponseEntity<>(new Recipe_DTO(recipe), HttpStatus.OK);
    }

    public ResponseEntity<Recipe_DTO> changeStatus(String recipeId) {
        Recipe recipe = recipeDAO.findById(Integer.parseInt(recipeId)).get();
        Status status = recipe.getStatus();
        if (status == Status.UNCHECKED) {
            recipe.setStatus(Status.CHECKED);
        } else {
            recipe.setStatus(Status.UNCHECKED);
        }
        recipeDAO.save(recipe);
        return new ResponseEntity<>(new Recipe_DTO(recipe), HttpStatus.OK);
    }


//=============================================================================================
//    Additional methods

    private int verifyTo(int to, long numberOfAllRecipes) {
        if (to >= numberOfAllRecipes) {
            to = (int) numberOfAllRecipes;
        }
        return to;
    }

    private void choseQuantityInRecipeByNutrient(int nutrientId, List<NutrientQuantityInRecipePer100Gramm> chosenRaw, List<Recipe> allByTitleAndCategory) {
        allByTitleAndCategory.forEach(recipe -> chosenRaw.add(quantityDAO100.findByRecipeAndNutrientId(recipe, nutrientId)));
    }

    private void sortChosen(List<NutrientQuantityInRecipePer100Gramm> chosenRaw) {
        chosenRaw.sort((o1, o2) -> (int) (o2.getQuantity() - o1.getQuantity()));
    }

    private List<Recipe_DTO> getChosenDtosFromNQPer100(int from, int to, List<NutrientQuantityInRecipePer100Gramm> chosenRaw) {
        return chosenRaw.stream().map(raw -> new Recipe_DTO(raw.getRecipe())).collect(Collectors.toList()).subList(from, to);
    }

    private List<Recipe_DTO> getChosenDtosFromRecipes(int from, int to, List<Recipe> allByParams) {
        return allByParams.stream().map(Recipe_DTO::new).collect(Collectors.toList()).subList(from, to);
    }

    private List<Recipe_DTO> getDtosFromRecipes(List<Recipe> allByTitle) {
        return allByTitle.stream().map(Recipe_DTO::new).collect(Collectors.toList());
    }

    private String getPathOfRecipeDir(String dateOfCreation) {
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
        return path + dateOfCreation;
    }

    private long getCount(List<Recipe> recipes) {
        return recipes.stream().count();
    }

    private int getTotalPages(long numberOfAllRecipes, int pageSize) {
        return (int) Math.ceil((double) numberOfAllRecipes / pageSize);
    }

    private ResponseEntity<WrapperForRecipes_DTO> getWrapperForRecipes_dtoResponseEntity(int pageNumber, int pageSize, List<Recipe> allByParams) {
        int from = pageSize * pageNumber;
        int to = from + pageSize;
        long numberOfAllRecipes = getCount(allByParams);
        int totalPages = getTotalPages(numberOfAllRecipes, pageSize);

        to = verifyTo(to, numberOfAllRecipes);
        List<Recipe_DTO> allRecipes = allByParams.subList(from, to).stream().map(Recipe_DTO::new).collect(Collectors.toList());

        return new ResponseEntity<>(new WrapperForRecipes_DTO(numberOfAllRecipes, allRecipes, totalPages, pageNumber), HttpStatus.OK);
    }
}