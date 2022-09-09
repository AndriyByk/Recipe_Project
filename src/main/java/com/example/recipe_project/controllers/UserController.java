package com.example.recipe_project.controllers;

import com.example.recipe_project.models.dto.categories_dto.ActivityType_DTO;
import com.example.recipe_project.models.dto.categories_dto.Gender_DTO;
import com.example.recipe_project.models.dto.entities_dto.User_DTO;
import com.example.recipe_project.models.entity.entities.User;
import com.example.recipe_project.services.entities.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@AllArgsConstructor
public class UserController {
    private UserService userService;

    @GetMapping("")
    public ResponseEntity<List<User_DTO>> findAllUsers(
            @RequestParam(required = false) int pageNumber,
            @RequestParam(required = false) int pageSize
    ) {
        return userService.findAllUsers(pageNumber, pageSize);
    }

    @GetMapping("/{id}")
    public ResponseEntity<User_DTO> findUserById(@PathVariable int id) {
        return userService.findUserById(id);
    }

    //////////////// важливий!!!! але поки прибрав, поки тестив стосунок фейворіт і кріейтед ресайпс
//    @PatchMapping("/{id}")
//    public ResponseEntity<User_DTO> updateUserById(@PathVariable int id, @RequestBody User user) {
//        return userService.updateUserById(id, user);
//    }

//    @PatchMapping("/{username}")
//    public ResponseEntity<User_DTO> updateUserByUsername(@PathVariable String username, @RequestBody User user) {
//        return userService.updateUserByUsername(username, user);
//    }

    @PatchMapping("/update/{username}")
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

    @DeleteMapping("/{id}")
    public ResponseEntity<List<User_DTO>> deleteUser(
            @PathVariable int id
    ) {
        return userService.deleteUser(id);
    }

    @GetMapping("/activity-types")
    public ResponseEntity<List<ActivityType_DTO>> findAllActivityTypes() {
        return userService.findAllActivityTypes();
    }

    @GetMapping("/genders")
    public ResponseEntity<List<Gender_DTO>> findAllGenders() {
        return userService.findAllGenders();
    }
}
