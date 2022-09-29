package com.example.recipe_project.services.entities;

import com.example.recipe_project.dao.entities_dao.INutrientDAO;
import com.example.recipe_project.dao.categories_dao.INutrientCategoryDAO;
import com.example.recipe_project.models.dto.categories_dto.NutrientCategory_DTO;
import com.example.recipe_project.models.dto.entities_dto.Nutrient_DTO;
import com.example.recipe_project.models.entity.entities.Nutrient;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class NutrientService {
    private INutrientDAO nutrientDAO;
    private INutrientCategoryDAO nutrientCategoryDAO;

    public ResponseEntity<List<Nutrient_DTO>> findAllNutrients() {
        return new ResponseEntity<>(nutrientDAO.findAll()
                .stream().map(Nutrient_DTO::new)
                .collect(Collectors.toList()), HttpStatus.OK);
    }

    public ResponseEntity<Nutrient_DTO> findNutrientById(int id) {
        Nutrient nutrient = nutrientDAO.findById(id).orElse(new Nutrient());
        if (nutrient.getId() == 0) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(new Nutrient_DTO(nutrient), HttpStatus.OK);
    }

    public ResponseEntity<Nutrient_DTO> updateNutrient(int id, Nutrient nutrient) {
        Nutrient nutrientFromDB = nutrientDAO.findById(id).get();
        if (nutrient.getName() != null)
            nutrientFromDB.setName(nutrient.getName());
        if (nutrient.getNutrientCategory() != null)
            nutrientFromDB.setNutrientCategory(nutrient.getNutrientCategory());
        if (nutrient.getAbout() != null)
            nutrientFromDB.setAbout(nutrient.getAbout());
        if (nutrient.getName() == null &&
                nutrient.getNutrientCategory() == null &&
                nutrient.getAbout() == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } else {
            nutrientDAO.save(nutrientFromDB);
            return new ResponseEntity<>(new Nutrient_DTO(nutrientFromDB), HttpStatus.OK);
        }
    }

    public ResponseEntity<List<Nutrient_DTO>> saveNutrient(Nutrient nutrient,
                                                           int pageNumber,
                                                           int pageSize) {
        if (nutrient != null) {
            nutrientDAO.save(nutrient);
            return new ResponseEntity<>(nutrientDAO
                    .findAll(PageRequest.of(pageNumber, pageSize)).getContent()
                    .stream().map(Nutrient_DTO::new)
                    .collect(Collectors.toList()), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(nutrientDAO
                    .findAll(PageRequest.of(pageNumber, pageSize)).getContent()
                    .stream().map(Nutrient_DTO::new)
                    .collect(Collectors.toList()), HttpStatus.BAD_REQUEST);
        }
    }

    public ResponseEntity<List<Nutrient_DTO>> deleteNutrient(int id,
                                                             int pageNumber,
                                                             int pageSize) {
        if (nutrientDAO.findAll().stream().anyMatch(nutrient -> nutrient.getId() == id)) {
            nutrientDAO.deleteById(id);
            return new ResponseEntity<>(nutrientDAO
                    .findAll(PageRequest.of(pageNumber, pageSize)).getContent()
                    .stream()
                    .map(Nutrient_DTO::new).collect(Collectors.toList()), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(nutrientDAO
                    .findAll(PageRequest.of(pageNumber, pageSize)).getContent()
                    .stream()
                    .map(Nutrient_DTO::new).collect(Collectors.toList()), HttpStatus.BAD_REQUEST);
        }
    }

    /////////////////////////////////////

    public ResponseEntity<List<NutrientCategory_DTO>> findAllNutrientCategories() {
        return new ResponseEntity<>(nutrientCategoryDAO.findAll()
                .stream()
                .map(NutrientCategory_DTO::new)
                .collect(Collectors.toList()), HttpStatus.OK);
    }

    /////////////////////////////////////

    public ResponseEntity<List<Nutrient_DTO>> findNutrientsByCategory(int id,
                                                                      int pageNumber,
                                                                      int pageSize) {
        int from = pageSize * pageNumber;
        int to = from + pageSize;
        return new ResponseEntity<>(nutrientCategoryDAO
                .findById(id)
                .get()
                .getNutrients().subList(from, to)
                .stream()
                .map(Nutrient_DTO::new)
                .collect(Collectors.toList()), HttpStatus.OK);
    }
}
