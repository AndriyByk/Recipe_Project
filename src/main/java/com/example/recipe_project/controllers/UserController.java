package com.example.recipe_project.controllers;

import com.example.recipe_project.models.dto.categories_dto.ActivityType_DTO;
import com.example.recipe_project.models.dto.categories_dto.Gender_DTO;
import com.example.recipe_project.models.dto.entities_dto.User_DTO;
import com.example.recipe_project.models.entity.entities.User;
import com.example.recipe_project.services.entities.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
//@RequestMapping("/users")
@AllArgsConstructor
public class UserController {
    private UserService userService;

    @GetMapping("/users")
    public ResponseEntity<List<User_DTO>> findAllUsers(
            @RequestParam(required = false) int pageNumber,
            @RequestParam(required = false) int pageSize
    ) {
        return userService.findAllUsers(pageNumber, pageSize);
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<User_DTO> findUserById(@PathVariable int id) {
        return userService.findUserById(id);
    }

    @PatchMapping("/users/norms/{username}")
    public ResponseEntity<User_DTO> calculateNorms(@PathVariable String username, @RequestBody User user) {
        return userService.calculateNorms(username, user);
    }

    //////////////// важливий!!!! але поки прибрав, поки тестив стосунок фейворіт і кріейтед ресайпс
    @PatchMapping("users/{id}")
    public ResponseEntity<User_DTO> updateUserById(@PathVariable int id, @RequestBody User user) {
        return userService.updateUserById(id, user);
    }


    @PatchMapping("user/{username}")
    public ResponseEntity<User_DTO> updateUserByUsername(
            @RequestParam String user,
            @RequestParam(required = false) MultipartFile avatar,
            @PathVariable String username) throws IOException {
        return userService.updateUserByUsername(user, avatar, username);
    }
//    @PatchMapping("users/{username}")
//    public ResponseEntity<User_DTO> updateUserByUsername(@PathVariable String username, @RequestBody User user) {
//        return userService.updateUserByUsername(username, user);
//    }

    @PatchMapping("/users/update/{username}")
    public ResponseEntity<User_DTO> updateFavoriteRecipes(@PathVariable String username, @RequestBody String recipeId) {
        return userService.updateFavoriteRecipes(username, recipeId);
    }


    // POST див. на MainController "/sign-up"
//    @PostMapping("")
//    public ResponseEntity<List<User_DTO>> saveUser(
//            @RequestParam String user,
//            @RequestParam(required = false) int pageNumber,
//            @RequestParam(required = false) int pageSize,
//            @RequestParam(required = false) MultipartFile avatar
//    ) throws IOException {
//        return userService.saveUser(user, avatar, pageNumber, pageSize);
//    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<List<User_DTO>> deleteUser(
            @PathVariable int id
    ) {
        return userService.deleteUser(id);
    }

    @GetMapping("/users/activity-types")
    public ResponseEntity<List<ActivityType_DTO>> findAllActivityTypes() {
        return userService.findAllActivityTypes();
    }

    @GetMapping("/users/genders")
    public ResponseEntity<List<Gender_DTO>> findAllGenders() {
        return userService.findAllGenders();
    }
}
