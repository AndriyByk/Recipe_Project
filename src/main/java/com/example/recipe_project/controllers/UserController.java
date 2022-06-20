package com.example.recipe_project.controllers;

import com.example.recipe_project.dao.IUserDAO;
import com.example.recipe_project.models.entity.User;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@AllArgsConstructor
public class UserController {
    private IUserDAO userDAO;

    @GetMapping("")
    public ResponseEntity<List<User>> findAllUsers() {
        return new ResponseEntity<>(userDAO.findAll(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> findUserById(@PathVariable int id) {
        User user = userDAO.findById(id).orElse(new User());
        if (user.getId() == 0) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable int id, @RequestBody User user) {
        User userFromDB = userDAO.findById(id).get();
        if (user.getName() != null)
            userFromDB.setName(user.getName());
        if (user.getEmail() != null)
            userFromDB.setEmail(user.getEmail());
        if (user.getWeight() != 0)
            userFromDB.setWeight(user.getWeight());
        if (user.getAge() != 0)
            userFromDB.setAge(user.getAge());
        if (user.getGender() != null)
            userFromDB.setGender(user.getGender());
        if (user.getActivityType() != null)
            userFromDB.setActivityType(user.getActivityType());
        if (user.getLastName() != null)
            userFromDB.setLastName(user.getLastName());
        if (user.getName() == null &&
                user.getEmail() == null &&
                user.getWeight() == 0 &&
                user.getHeight() == 0 &&
                user.getAge() == 0 &&
                user.getGender() == null &&
                user.getActivityType() == null &&
                user.getLastName() == null &&
                user.getDateOfRegistration() == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } else {
            userDAO.save(userFromDB);
            return new ResponseEntity<>(userFromDB, HttpStatus.OK);
        }
    }

    @PostMapping("")
    public ResponseEntity<List<User>> saveUser(@RequestBody User user) {
        if (user != null) {
            userDAO.save(user);
            return new ResponseEntity<>(userDAO.findAll(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(userDAO.findAll(), HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<List<User>> deleteUser(@PathVariable int id) {
        if (id != 0) {
            userDAO.deleteById(id);
            return new ResponseEntity<>(userDAO.findAll(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(userDAO.findAll(), HttpStatus.BAD_REQUEST);
        }
    }
}
