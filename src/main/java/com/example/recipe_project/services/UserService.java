package com.example.recipe_project.services;

import com.example.recipe_project.dao.IUserDAO;
import com.example.recipe_project.dao.categories_dao.IActivityTypeDAO;
import com.example.recipe_project.dao.categories_dao.IGenderDAO;
import com.example.recipe_project.models.dto.categories_dto.ActivityType_DTO;
import com.example.recipe_project.models.dto.categories_dto.Gender_DTO;
import com.example.recipe_project.models.dto.entities_dto.User_DTO;
import com.example.recipe_project.models.entity.entities.Rank;
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
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class UserService implements UserDetailsService {
    private IUserDAO userDAO;
    private IGenderDAO genderDAO;
    private IActivityTypeDAO activityTypeDAO;
    private PasswordEncoder passwordEncoder;

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

    public ResponseEntity<User_DTO> updateUser(int id, User user) {
        if (userDAO.findAll().stream().allMatch(userDAO -> userDAO.getId() != id)) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        User userFromDB = userDAO.findById(id).get();
        if (user.getName() != null)
            userFromDB.setName(user.getName());
        if (user.getEmail() != null)
            userFromDB.setEmail(user.getEmail());
        if (user.getWeight() != 0)
            userFromDB.setWeight(user.getWeight());
        if (user.getDayOfBirth() != null)
            userFromDB.setDayOfBirth(user.getDayOfBirth());
        if (user.getGender() != null)
            userFromDB.setGender(user.getGender());
        if (user.getActivityType() != null)
            userFromDB.setActivityType(user.getActivityType());
        if (user.getLastName() != null)
            userFromDB.setLastName(user.getLastName());
        if (user.getFavoriteRecipes() != null && user.getFavoriteRecipes().size() != 0) {
            List<Recipe> newRecipes = user.getFavoriteRecipes();
            for (Recipe newRecipe : newRecipes) {
                List<Recipe> recipesFromDB = userFromDB.getFavoriteRecipes();
                for (int j = 0; j < recipesFromDB.size(); j++) {
                    Recipe recipeFromDB = recipesFromDB.get(j);
                    if (newRecipe.getId() == recipeFromDB.getId()) {
                        break;
                    }
                    if (j == recipesFromDB.size() - 1) {
                        recipesFromDB.add(newRecipe);
                    }
                }
            }
        }
        if (user.getCreatedRecipes() != null && user.getCreatedRecipes().size() != 0) {
            List<Recipe> newRecipes = user.getCreatedRecipes();
            for (Recipe newRecipe : newRecipes) {
                List<Recipe> recipesFromDB = userFromDB.getCreatedRecipes();
                for (int j = 0; j < recipesFromDB.size(); j++) {
                    Recipe recipeFromDB = recipesFromDB.get(j);
                    if (newRecipe.getId() == recipeFromDB.getId()) {
                        break;
                    }
                    if (j == recipesFromDB.size() - 1) {
                        recipesFromDB.add(newRecipe);
                    }
                }
            }
        }
        if (user.getRanks() != null && user.getRanks().size() != 0) {
            List<Rank> newRanks = user.getRanks();
            for (Rank newRank : newRanks) {
                List<Rank> ranksFromDB = userFromDB.getRanks();
                for (int i = 0; i < ranksFromDB.size(); i++) {
                    Rank rankFromDB = ranksFromDB.get(i);
                    if (newRank.getId() == rankFromDB.getId()) {
                        break;
                    }
                    if (i == ranksFromDB.size() - 1) {
                        ranksFromDB.add(newRank);
                    }
                }
            }
        }

        if (user.getName() == null &&
                user.getEmail() == null &&
                user.getWeight() == 0 &&
                user.getHeight() == 0 &&
                user.getDayOfBirth() == null &&
                user.getGender() == null &&
                user.getActivityType() == null &&
                user.getLastName() == null &&
                user.getRanks() == null &&
                user.getFavoriteRecipes() == null &&
                user.getCreatedRecipes() == null &&
                user.getDateOfRegistration() == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } else {
            userDAO.save(userFromDB);
            return new ResponseEntity<>(new User_DTO(userFromDB), HttpStatus.OK);
        }
    }

    public ResponseEntity<List<User_DTO>> saveUser(
            String user,
            MultipartFile avatar,
            int pageNumber,
            int pageSize) throws IOException {
        System.out.println(pageNumber);
        System.out.println(pageSize);
        System.out.println(avatar.getOriginalFilename());
        if (user != null) {
            if (user instanceof String) {
                System.out.println(user.getClass());
            }

            RawUser rawUser = new ObjectMapper().readValue(user, RawUser.class);

            // збереження картинки
            String path = System.getProperty("user.home") + File.separator + "Pictures" + File.separator;
            avatar.transferTo(new File(path + avatar.getOriginalFilename()));

            List<Recipe> favoriteRecipes = new ArrayList<>();
            List<Recipe> createdRecipes = new ArrayList<>();
            List<Rank> ranks = new ArrayList<>();

            User userForDB = new User(
                    rawUser.getUsername(),
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
                    favoriteRecipes,
                    createdRecipes,
                    ranks
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

    public ResponseEntity<List<User_DTO>> deleteUser(int id, int pageNumber, int pageSize) {
        if (userDAO.findAll().stream().anyMatch(recipeDAO -> recipeDAO.getId() == id)) {
            userDAO.deleteById(id);
            return new ResponseEntity<>(userDAO.findAll(PageRequest.of(pageNumber, pageSize)).getContent().stream().map(User_DTO::new)
                    .collect(Collectors.toList()), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(userDAO.findAll(PageRequest.of(pageNumber, pageSize)).getContent().stream().map(User_DTO::new)
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
}
