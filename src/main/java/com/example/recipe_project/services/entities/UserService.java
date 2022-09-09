package com.example.recipe_project.services.entities;

import com.example.recipe_project.dao.entities_dao.IRecipeDAO;
import com.example.recipe_project.dao.entities_dao.IUserDAO;
import com.example.recipe_project.dao.categories_dao.IActivityTypeDAO;
import com.example.recipe_project.dao.categories_dao.IGenderDAO;
import com.example.recipe_project.dao.mediate_dao.IFavoriteRecipeDAO;
import com.example.recipe_project.models.dto.categories_dto.ActivityType_DTO;
import com.example.recipe_project.models.dto.categories_dto.Gender_DTO;
import com.example.recipe_project.models.dto.entities_dto.User_DTO;
import com.example.recipe_project.models.entity.entities.FavoriteRecipe;
import com.example.recipe_project.models.entity.raw.RawUser;
import com.example.recipe_project.models.entity.entities.Recipe;
import com.example.recipe_project.models.entity.entities.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class UserService implements UserDetailsService {
    private IUserDAO userDAO;
    private IGenderDAO genderDAO;
    private IActivityTypeDAO activityTypeDAO;
    private PasswordEncoder passwordEncoder;
    private IFavoriteRecipeDAO favoriteRecipeDAO;

    private IRecipeDAO recipeDAO;

    public ResponseEntity<List<User_DTO>> findAllUsers(int pageNumber, int pageSize) {
        List<User_DTO> allUsers_dto = userDAO
                .findAll(PageRequest.of(pageNumber, pageSize)).getContent()
                .stream().map(User_DTO::new)
                .collect(Collectors.toList());
        return new ResponseEntity<>(allUsers_dto, HttpStatus.OK);
    }

    public ResponseEntity<User_DTO> findUserById(int id) {
        User user = userDAO.findById(id).orElse(new User());
        if (user.getId() == 0) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(new User_DTO(user), HttpStatus.OK);
    }

    //////////////// важливий!!!! але поки прибрав, поки тестив стосунок фейворіт і кріейтед ресайпс

//    public ResponseEntity<User_DTO> updateUserById(int id, User user) {
//        if (userDAO.findAll().stream().allMatch(userDAO -> userDAO.getId() != id)) {
//            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
//        }
//        User userFromDB = userDAO.findById(id).get();
//        if (user.getName() != null)
//            userFromDB.setName(user.getName());
//        if (user.getEmail() != null)
//            userFromDB.setEmail(user.getEmail());
//        if (user.getWeight() != 0)
//            userFromDB.setWeight(user.getWeight());
//        if (user.getDayOfBirth() != null)
//            userFromDB.setDayOfBirth(user.getDayOfBirth());
//        if (user.getGender() != null)
//            userFromDB.setGender(user.getGender());
//        if (user.getActivityType() != null)
//            userFromDB.setActivityType(user.getActivityType());
//        if (user.getLastName() != null)
//            userFromDB.setLastName(user.getLastName());
//        if (user.getFavoriteRecipes() != null && user.getFavoriteRecipes().size() != 0) {
//            List<Recipe> newRecipes = user.getFavoriteRecipes();
//            for (Recipe newRecipe : newRecipes) {
//                List<Recipe> recipesFromDB = userFromDB.getFavoriteRecipes();
//                for (int j = 0; j < recipesFromDB.size(); j++) {
//                    Recipe recipeFromDB = recipesFromDB.get(j);
//                    if (newRecipe.getId() == recipeFromDB.getId()) {
//                        break;
//                    }
//                    if (j == recipesFromDB.size() - 1) {
//                        recipesFromDB.add(newRecipe);
//                    }
//                }
//            }
//        }
//        if (user.getCreatedRecipes() != null && user.getCreatedRecipes().size() != 0) {
//            List<Recipe> newRecipes = user.getCreatedRecipes();
//            for (Recipe newRecipe : newRecipes) {
//                List<Recipe> recipesFromDB = userFromDB.getCreatedRecipes();
//                for (int j = 0; j < recipesFromDB.size(); j++) {
//                    Recipe recipeFromDB = recipesFromDB.get(j);
//                    if (newRecipe.getId() == recipeFromDB.getId()) {
//                        break;
//                    }
//                    if (j == recipesFromDB.size() - 1) {
//                        recipesFromDB.add(newRecipe);
//                    }
//                }
//            }
//        }
//        if (user.getRanks() != null && user.getRanks().size() != 0) {
//            List<Rank> newRanks = user.getRanks();
//            for (Rank newRank : newRanks) {
//                List<Rank> ranksFromDB = userFromDB.getRanks();
//                for (int i = 0; i < ranksFromDB.size(); i++) {
//                    Rank rankFromDB = ranksFromDB.get(i);
//                    if (newRank.getId() == rankFromDB.getId()) {
//                        break;
//                    }
//                    if (i == ranksFromDB.size() - 1) {
//                        ranksFromDB.add(newRank);
//                    }
//                }
//            }
//        }
//
//        if (user.getName() == null &&
//                user.getEmail() == null &&
//                user.getWeight() == 0 &&
//                user.getHeight() == 0 &&
//                user.getDayOfBirth() == null &&
//                user.getGender() == null &&
//                user.getActivityType() == null &&
//                user.getLastName() == null &&
//                user.getRanks() == null &&
//                user.getFavoriteRecipes() == null &&
//                user.getCreatedRecipes() == null &&
//                user.getDateOfRegistration() == null) {
//            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
//        } else {
//            userDAO.save(userFromDB);
//            return new ResponseEntity<>(new User_DTO(userFromDB), HttpStatus.OK);
//        }
//    }

//    public ResponseEntity<User_DTO> updateUserByUsername(String username, User user) {
//        if (userDAO.findAll().stream().noneMatch(userDAO -> userDAO.getUsername().equals(username))) {
//            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
//        }
//        User userFromDB = userDAO.findByUsername(username);
//        if (user.getName() != null)
//            userFromDB.setName(user.getName());
//        if (user.getEmail() != null && !user.getEmail().equals(""))
//            userFromDB.setEmail(user.getEmail());
//        if (user.getWeight() != 0)
//            userFromDB.setWeight(user.getWeight());
//        if (user.getDayOfBirth() != null && !user.getDayOfBirth().equals(""))
//            userFromDB.setDayOfBirth(user.getDayOfBirth());
//        if (user.getGender() != null && !user.getGender().equals(""))
//            userFromDB.setGender(user.getGender());
//        if (user.getActivityType() != null)
//            userFromDB.setActivityType(user.getActivityType());
//        if (user.getLastName() != null)
//            userFromDB.setLastName(user.getLastName());
//        if (user.getFavoriteRecipes() != null && user.getFavoriteRecipes().size() != 0) {
//            List<Recipe> newRecipes = user.getFavoriteRecipes();
//            for (Recipe newRecipe : newRecipes) {
//                List<Recipe> recipesFromDB = userFromDB.getFavoriteRecipes();
//                for (int j = 0; j < recipesFromDB.size(); j++) {
//                    Recipe recipeFromDB = recipesFromDB.get(j);
//                    if (newRecipe.getId() == recipeFromDB.getId()) {
//                        break;
//                    }
//                    if (j == recipesFromDB.size() - 1) {
//                        recipesFromDB.add(newRecipe);
//                    }
//                }
//            }
//        }
//        if (user.getCreatedRecipes() != null && user.getCreatedRecipes().size() != 0) {
//            List<Recipe> newRecipes = user.getCreatedRecipes();
//            for (Recipe newRecipe : newRecipes) {
//                List<Recipe> recipesFromDB = userFromDB.getCreatedRecipes();
//                for (int j = 0; j < recipesFromDB.size(); j++) {
//                    Recipe recipeFromDB = recipesFromDB.get(j);
//                    if (newRecipe.getId() == recipeFromDB.getId()) {
//                        break;
//                    }
//                    if (j == recipesFromDB.size() - 1) {
//                        recipesFromDB.add(newRecipe);
//                    }
//                }
//            }
//        }
//        if (user.getRanks() != null && user.getRanks().size() != 0) {
//            List<Rank> newRanks = user.getRanks();
//            for (Rank newRank : newRanks) {
//                List<Rank> ranksFromDB = userFromDB.getRanks();
//                for (int i = 0; i < ranksFromDB.size(); i++) {
//                    Rank rankFromDB = ranksFromDB.get(i);
//                    if (newRank.getId() == rankFromDB.getId()) {
//                        break;
//                    }
//                    if (i == ranksFromDB.size() - 1) {
//                        ranksFromDB.add(newRank);
//                    }
//                }
//            }
//        }
//
//        if (user.getName() == null &&
//                user.getEmail() == null &&
//                user.getWeight() == 0 &&
//                user.getHeight() == 0 &&
//                user.getDayOfBirth() == null &&
//                user.getGender() == null &&
//                user.getActivityType() == null &&
//                user.getLastName() == null &&
//                user.getRanks() == null &&
//                user.getFavoriteRecipes() == null &&
//                user.getCreatedRecipes() == null &&
//                user.getDateOfRegistration() == null) {
//            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
//        } else {
//            userDAO.save(userFromDB);
//            return new ResponseEntity<>(new User_DTO(userFromDB), HttpStatus.OK);
//        }
//    }

    public ResponseEntity<List<User_DTO>> saveUser(String user, MultipartFile avatar, int pageNumber, int pageSize) throws IOException {

        if (user != null) {
            RawUser rawUser = new ObjectMapper().readValue(user, RawUser.class);

            // збереження картинки
            String path =
                    System.getProperty("user.home") + File.separator
                            + "IdeaProjects" + File.separator
                            + "Recipe_Project" + File.separator
                            + "src" + File.separator
                            + "main" + File.separator
                            + "java" + File.separator
                            + "com" + File.separator
                            + "example" + File.separator
                            + "recipe_project" + File.separator
                            + "pictures" + File.separator
                            + "users" + File.separator;

            String pathOfUserDir = path + rawUser.getUsername();

            if (new File(pathOfUserDir).mkdir()) {
                avatar.transferTo(new File(pathOfUserDir + File.separator + avatar.getOriginalFilename()));
            }

            User userForDB = new User(
                    rawUser.getUsername(),
                    // закодовка пароля
                    passwordEncoder.encode(rawUser.getPassword()),
                    avatar.getOriginalFilename(),
                    rawUser.getEmail(),
                    rawUser.getWeight(),
                    rawUser.getHeight(),
                    rawUser.getDayOfBirth(),
                    genderDAO.findById(rawUser.getGenderId()).get(),
                    activityTypeDAO.findById(rawUser.getActivityTypeId()).get(),
                    rawUser.getName(),
                    rawUser.getLastName(),
                    rawUser.getDateOfRegistration(),
                    new ArrayList<>(),
                    new ArrayList<>(),
                    new ArrayList<>(),
                    new HashSet<>()
            );
            userDAO.save(userForDB);

            return new ResponseEntity<>(userDAO.findAll(PageRequest.of(pageNumber, pageSize)).getContent()
                    .stream().map(User_DTO::new)
                    .collect(Collectors.toList()), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(userDAO.findAll(PageRequest.of(pageNumber, pageSize)).getContent()
                    .stream().map(User_DTO::new)
                    .collect(Collectors.toList()), HttpStatus.BAD_REQUEST);
        }
    }

    public void saveUser(User user) {
        userDAO.save(user);
    }

    public ResponseEntity<List<User_DTO>> deleteUser(int id) {
        if (userDAO.findAll().stream().anyMatch(recipeDAO -> recipeDAO.getId() == id)) {
            System.out.println("before ------------ " + id);
            userDAO.deleteById(id);
            System.out.println("after ------------ " + id);
            return new ResponseEntity<>(userDAO.findAll().stream().map(User_DTO::new)
                    .collect(Collectors.toList()), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(userDAO.findAll().stream().map(User_DTO::new)
                    .collect(Collectors.toList()), HttpStatus.BAD_REQUEST);
        }
    }

    public ResponseEntity<List<ActivityType_DTO>> findAllActivityTypes() {
        return new ResponseEntity<>(activityTypeDAO
                .findAll()
                .stream()
                .map(ActivityType_DTO::new)
                .collect(Collectors.toList()), HttpStatus.OK);
    }

    public ResponseEntity<List<Gender_DTO>> findAllGenders() {
        return new ResponseEntity<>(genderDAO
                .findAll()
                .stream()
                .map(Gender_DTO::new)
                .collect(Collectors.toList()), HttpStatus.OK);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userDAO.findByUsername(username);
    }

    public User findByName(String name) {
        return userDAO.findByUsername(name);
    }

    public ResponseEntity<User_DTO> findByUsername(String name) {
        User user = userDAO.findByUsername(name);
        System.out.println(user);
        user.getFavoriteRecipes().forEach(favoriteRecipe -> {
            System.out.println("-----" + favoriteRecipe.getId().getRecipe_id());
            System.out.println(favoriteRecipe.getId().getUser_id());
        });
        return new ResponseEntity<>(new User_DTO(user), HttpStatus.OK);
    }

    public ResponseEntity<User_DTO> updateFavoriteRecipes(String username, String recipeId) {
        User user = userDAO.findByUsername(username);
        Recipe recipe = recipeDAO.findById(Integer.parseInt(recipeId)).get();

        AtomicBoolean b = new AtomicBoolean(false);
        favoriteRecipeDAO.findAll().forEach(favoriteRecipe -> {

            if (favoriteRecipe.getRecipe().getId() == recipe.getId()
                    && favoriteRecipe.getUser().getId() == user.getId()) {
                favoriteRecipeDAO.delete(favoriteRecipe);
                System.out.println("favoriteRecipe was deleted");
                b.set(true);
            }
        });
        if (!b.get()) {
            favoriteRecipeDAO.save(new FavoriteRecipe(user, recipe));
            System.out.println("favoriteRecipe was saved");
        }

//        user.getFavoriteRecipes().add(new FavoriteRecipe(recipeDAO.findById(Integer.parseInt(recipeId)).get()));
//        userDAO.save(user);
        return new ResponseEntity<>(new User_DTO(user), HttpStatus.OK);
    }
}
