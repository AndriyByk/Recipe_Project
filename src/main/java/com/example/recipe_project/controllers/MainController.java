package com.example.recipe_project.controllers;

import com.example.recipe_project.models.dto.entities_dto.User_DTO;
import com.example.recipe_project.models.dto.sign_in.UserAccessToken_DTO;
import com.example.recipe_project.models.entity.auth.AuthToken;
import com.example.recipe_project.services.authorisation.TokenService;
import com.example.recipe_project.services.entities.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@AllArgsConstructor
public class MainController {
    private UserService userService;
    private TokenService tokenService;

    @GetMapping("/")
    public String home() {
        return "it is home page";
    }

    @PostMapping("/sign-up")
    public ResponseEntity<List<User_DTO>> saveUser(
            @RequestParam String user,
            @RequestParam(required = false) int pageNumber,
            @RequestParam(required = false) int pageSize,
            @RequestParam(required = false) MultipartFile avatar
    ) throws IOException {
        return userService.saveUser(user, avatar, pageNumber, pageSize);
    }

    @PostMapping("/sign-in")
    public UserAccessToken_DTO signIn() {
        // заглушка (головна інфа йде в хедері authorisation)
        return new UserAccessToken_DTO();
    }

    @GetMapping("/cabinet/{username}")
    public ResponseEntity<User_DTO> getUserByUsername(@PathVariable String username) {
        return userService.findByUsername(username);
    }

    @DeleteMapping("/cabinet/{access}")
    public AuthToken deleteTokenFromDB(
            @PathVariable String access
    ) {
        System.out.println(access);
        String bearer = access.replace("Bearer ", "");
        tokenService.deleteAuthTokenByToken(bearer);
        return new AuthToken();
    }

}
