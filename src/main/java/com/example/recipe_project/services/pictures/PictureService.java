package com.example.recipe_project.services.pictures;

import com.example.recipe_project.dao.entities_dao.IUserDAO;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class PictureService {
    private IUserDAO userDAO;

    public String getUserAvatar(int id) {
        return userDAO.findById(id).get().getAvatar();
    }
}
