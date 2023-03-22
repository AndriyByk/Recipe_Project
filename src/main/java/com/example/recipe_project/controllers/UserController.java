package com.example.recipe_project.controllers;

import com.example.recipe_project.models.dto.categories_dto.ActivityType_DTO;
import com.example.recipe_project.models.dto.categories_dto.Gender_DTO;
import com.example.recipe_project.models.dto.entities_dto.UserShort_DTO;
import com.example.recipe_project.models.dto.entities_dto.User_DTO;
import com.example.recipe_project.models.dto.wrappers_dto.WrapperForUsers_DTO;
import com.example.recipe_project.services.entities.UserService;
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

    @GetMapping("/users/{pageNumber}")
    public ResponseEntity<List<User_DTO>> findAllUsers(
            @PathVariable int pageNumber,
            @RequestParam(required = false) int pageSize
    ) {
        return userService.findAllUsers(pageNumber, pageSize);
    }

    @GetMapping("/users/chosen/{pageNumber}")
    public ResponseEntity<WrapperForUsers_DTO> findChosenUsers(
            @PathVariable int pageNumber,
            @RequestParam(required = false) int pageSize,
            @RequestParam(required = false) String username,
            @RequestParam(required = false) int role
    ) {
        return userService.findChosenUsers(pageNumber, pageSize, username, role);
    }

    @PatchMapping("/users/{userId}")
    public ResponseEntity<List<User_DTO>> changeRole(
            @RequestBody String s,
            @PathVariable int userId,
            @RequestParam int role,
            @RequestParam int pageNumber,
            @RequestParam int pageSize) {
        System.out.println("changeRole method in userController = " + s);
        return userService.changeRole(role, userId, pageNumber, pageSize);
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<UserShort_DTO> findUserById(@PathVariable int id) {
        return userService.findUserById(id);
    }
    
//    findUserByUsername див. в MainController

    @GetMapping("/users/rates")
    public ResponseEntity<Integer> getRate(
            @RequestParam int recipeId,
            @RequestParam int userId
    ) {
        return userService.getRate(recipeId, userId);
    }

    @PatchMapping("/users/norms/{username}")
    public ResponseEntity<User_DTO> calculateNorms(@PathVariable String username, @RequestBody String user) {
        return userService.calculateNorms(username);
    }

    @PatchMapping("/user/{username}")
    public ResponseEntity<User_DTO> updateUserByUsername(
            @RequestParam String user,
            @RequestParam(required = false) MultipartFile avatar,
            @PathVariable String username) throws IOException {
        return userService.updateUserByUsername(user, avatar, username);
    }

    @PatchMapping("/users/update/{username}")
    public ResponseEntity<User_DTO> updateFavoriteRecipes(@PathVariable String username, @RequestBody String recipeId) {
        return userService.updateFavoriteRecipes(username, recipeId);
    }

    // POST див. на MainController "/sign-up"

    @DeleteMapping("/users/{id}")
    public ResponseEntity<List<User_DTO>> deleteUserById(
            @PathVariable int id
    ) {
        return userService.deleteUserById(id);
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
