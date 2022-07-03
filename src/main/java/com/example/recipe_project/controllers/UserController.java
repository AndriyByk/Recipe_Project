package com.example.recipe_project.controllers;

import com.example.recipe_project.models.dto.entities_dto.User_DTO;
import com.example.recipe_project.models.entity.raw.RawUser;
import com.example.recipe_project.models.entity.entities.User;
import com.example.recipe_project.services.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
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

    @PatchMapping("/{id}")
    public ResponseEntity<User_DTO> updateUser(@PathVariable int id, @RequestBody User user) {
        return userService.updateUser(id, user);
    }

    @PostMapping("")
    public ResponseEntity<List<User_DTO>> saveUser(
            @RequestParam String user,
            @RequestParam(required = false) int pageNumber,
            @RequestParam(required = false) int pageSize,
            @RequestParam(required = false) MultipartFile avatar
    ) throws IOException {
        return userService.saveUser(user, avatar, pageNumber, pageSize);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<List<User_DTO>> deleteUser(
            @PathVariable int id,
            @RequestParam(required = false) int pageNumber,
            @RequestParam(required = false) int pageSize
    ) {
        return userService.deleteUser(id, pageNumber, pageSize);
    }
}
