package com.example.recipe_project.services.entities;

import com.example.recipe_project.dao.categories_dao.ITypeDAO;
import com.example.recipe_project.dao.entities_dao.INormDAO;
import com.example.recipe_project.dao.entities_dao.IRecipeDAO;
import com.example.recipe_project.dao.entities_dao.IUserDAO;
import com.example.recipe_project.dao.categories_dao.IActivityTypeDAO;
import com.example.recipe_project.dao.categories_dao.IGenderDAO;
import com.example.recipe_project.dao.mediate_dao.IFavoriteRecipeDAO;
import com.example.recipe_project.dao.mediate_dao.IRankDAO;
import com.example.recipe_project.models.dto.categories_dto.ActivityType_DTO;
import com.example.recipe_project.models.dto.categories_dto.Gender_DTO;
import com.example.recipe_project.models.dto.entities_dto.UserShort_DTO;
import com.example.recipe_project.models.dto.entities_dto.User_DTO;
import com.example.recipe_project.models.dto.wrappers_dto.WrapperForUsers_DTO;
import com.example.recipe_project.models.entity.categories.Role;
import com.example.recipe_project.models.entity.categories.norm.Type;
import com.example.recipe_project.models.entity.entities.*;
import com.example.recipe_project.models.entity.ids.UserNormId;
import com.example.recipe_project.models.entity.raw.RawUpdatedUser;
import com.example.recipe_project.models.entity.raw.RawUser;
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
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.*;
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
    private INormDAO normDAO;
    private ITypeDAO typeDAO;
    private IRankDAO rankDAO;

    public ResponseEntity<List<User_DTO>> findAllUsers(int pageNumber, int pageSize) {
        List<User_DTO> allUsers_dto = userDAO
                .findAll(PageRequest.of(pageNumber, pageSize)).getContent()
                .stream().map(User_DTO::new)
                .collect(Collectors.toList());
        return new ResponseEntity<>(allUsers_dto, HttpStatus.OK);
    }

    public ResponseEntity<WrapperForUsers_DTO> findChosenUsers(int pageNumber, int pageSize, String username, int role) {

        int from = pageSize * pageNumber;
        int to = from + pageSize;

        List<User> usersByParams;
        long numberOfAllUsers;
        int totalPages;

        if (!username.equals("undefined") && !username.equals("")) {
//            роль 100 == роль, якої не існує, тобто "без ролі"
            if (role != 100) {
                usersByParams = userDAO.findAllByRolesInAndUsernameContaining(Collections.singletonList(Role.values()[role]), username);
            } else {
                usersByParams = userDAO.findAllByUsernameContaining(username);
            }
        } else {
            if (role != 100) {
                usersByParams = userDAO.findAllByRolesIn(Collections.singletonList(Role.values()[role]));
            } else {
                usersByParams = userDAO.findAll();
            }
        }

        numberOfAllUsers = getCount(usersByParams);
        totalPages = getTotalPages(numberOfAllUsers, pageSize);
        to = verifyTo(to, numberOfAllUsers);
        List<User_DTO> chosenUsers = new ArrayList<>(getChosenDtosFromUsers(from, to, usersByParams));

        return new ResponseEntity<>(new WrapperForUsers_DTO(
                numberOfAllUsers,
                chosenUsers,
                totalPages,
                pageNumber
        ), HttpStatus.OK);
    }

    public ResponseEntity<List<User_DTO>> changeRole(int role, int userId, int pageNumber, int pageSize) {
        User user = userDAO.findById(userId).get();
        Role userRole = user.getRoles().get(0);
        switch (role) {
            case 0: {
                if (userRole.ordinal() != Role.ROLE_USER.ordinal()) {
                    user.getRoles().clear();
                    user.getRoles().add(Role.ROLE_USER);
                }
                break;
            }
            case 1: {
                if (userRole.ordinal() != Role.ROLE_ADMIN.ordinal()) {
                    user.getRoles().clear();
                    user.getRoles().add(Role.ROLE_ADMIN);
                }
            }
        }
        userDAO.save(user);

        return new ResponseEntity<>(
                userDAO.findAll(PageRequest.of(pageNumber, pageSize)).getContent()
                        .stream().map(User_DTO::new).collect(Collectors.toList()), HttpStatus.OK);
    }

    public ResponseEntity<UserShort_DTO> findUserById(int id) {
        User user = userDAO.findById(id).orElse(new User());
        if (user.getId() == 0) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(new UserShort_DTO(user), HttpStatus.OK);
    }

    public ResponseEntity<User_DTO> updateUserByUsername(String user, MultipartFile avatar, String username) throws IOException {
        if (user != null) {
            RawUpdatedUser newUser = new ObjectMapper().readValue(user, RawUpdatedUser.class);
            User userFromDB = userDAO.findByUsername(username);
            if (avatar != null) {
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
                String pathOfUserDir = path + username;
                new File(pathOfUserDir + File.separator + userFromDB.getAvatar()).delete();
                userFromDB.setAvatar(avatar.getOriginalFilename());
                avatar.transferTo(new File(pathOfUserDir + File.separator + avatar.getOriginalFilename()));
            }

            if (userDAO.findAll().stream().noneMatch(userDAO -> userDAO.getUsername().equals(username))) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }

            userFromDB.getNorms().clear();

            if (newUser.getName() != null && !newUser.getName().equals(""))
                userFromDB.setName(newUser.getName());
            if (newUser.getEmail() != null && !newUser.getEmail().equals(""))
                userFromDB.setEmail(newUser.getEmail());
            if (newUser.getWeight() != 0)
                userFromDB.setWeight(newUser.getWeight());
            if (newUser.getHeight() != 0)
                userFromDB.setHeight(newUser.getHeight());
            if (newUser.getDayOfBirth() != null && !newUser.getDayOfBirth().equals(""))
                userFromDB.setDayOfBirth(newUser.getDayOfBirth());
            if (newUser.getGenderId() != 0)
                userFromDB.setGender(genderDAO.findById(newUser.getGenderId()).get());
            if (newUser.getActivityTypeId() != 0)
                userFromDB.setActivityType(activityTypeDAO.findById(newUser.getActivityTypeId()).get());
            if (newUser.getLastName() != null && !newUser.getLastName().equals(""))
                userFromDB.setLastName(newUser.getLastName());

            if (newUser.getName() == null &&
                    newUser.getEmail() == null &&
                    newUser.getWeight() == 0 &&
                    newUser.getHeight() == 0 &&
                    newUser.getDayOfBirth() == null &&
                    newUser.getGenderId() == 0 &&
                    newUser.getActivityTypeId() == 0 &&
                    newUser.getLastName() == null) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            } else {
                userDAO.save(userFromDB);
                return new ResponseEntity<>(new User_DTO(userFromDB), HttpStatus.OK);
            }
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

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
                    new ArrayList<>(),
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
                    new HashSet<>(),
                    new ArrayList<>()
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

    public ResponseEntity<List<User_DTO>> deleteUserById(int id) {
        if (userDAO.findAll().stream().anyMatch(recipeDAO -> recipeDAO.getId() == id)) {

            User user = userDAO.findById(id).get();
            String path = System.getProperty("user.home") + File.separator
                    + "IdeaProjects" + File.separator
                    + "Recipe_Project" + File.separator
                    + "src" + File.separator
                    + "main" + File.separator
                    + "java" + File.separator
                    + "com" + File.separator
                    + "example" + File.separator
                    + "recipe_project" + File.separator
                    + "pictures" + File.separator
                    + "users" + File.separator
                    + user.getUsername();
            File file = new File(path + "/" + user.getAvatar());
            if (file.exists()) {
                file.delete();
                File directory = new File(path);
                if (directory.exists()) {
                    directory.delete();
                }
            }

            userDAO.deleteById(id);


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
        return new ResponseEntity<>(new User_DTO(user), HttpStatus.OK);
    }

    public ResponseEntity<User_DTO> updateFavoriteRecipes(String username, String recipeId) {
        User user = userDAO.findByUsername(username);
        Recipe recipe = recipeDAO.findById(Integer.parseInt(recipeId)).get();

        FavoriteRecipe byRecipeAndUser = favoriteRecipeDAO.findByRecipeAndUser(recipe, user);
        if (byRecipeAndUser != null) {
            favoriteRecipeDAO.delete(byRecipeAndUser);
        } else {
            favoriteRecipeDAO.save(new FavoriteRecipe(user, recipe));
        }

        return new ResponseEntity<>(new User_DTO(user), HttpStatus.OK);
    }

    public ResponseEntity<User_DTO> calculateNorms(String username) {
        User user = userDAO.findByUsername(username);

        // age
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate date1 = LocalDate.parse(user.getDayOfBirth(), formatter);
        LocalDate dateNow1 = LocalDate.now();
        Period period = Period.between(date1, dateNow1);
        double ageYear = period.getYears();
        double ageMonths = period.getMonths() / 12D;
        double ageDays = period.getDays() / 365.25D;
        double ageDouble = ageYear + ageMonths + ageDays;

        double ageSmall;
        double ageBig;

        if (ageDouble < 0.33) {
            ageSmall = 0.31;
            ageBig = 0.33;
        } else if (ageDouble < 0.59) {
            ageSmall = 0.57;
            ageBig = 0.59;
        } else if (ageDouble < 1) {
            ageSmall = 0.98;
            ageBig = 1.0;
        } else if (ageDouble < 4) {
            ageSmall = 3;
            ageBig = 4;
        } else if (ageDouble < 7) {
            ageSmall = 6;
            ageBig = 7;
        } else if (ageDouble < 11) {
            ageSmall = 10;
            ageBig = 11;
        } else if (ageDouble < 14) {
            ageSmall = 17;
            ageBig = 14;
        } else if (ageDouble < 30) {
            ageSmall = 29;
            ageBig = 30;
        } else if (ageDouble < 40) {
            ageSmall = 39;
            ageBig = 40;
        } else if (ageDouble < 60) {
            ageSmall = 59;
            ageBig = 60;
        } else if (ageDouble < 75) {
            ageSmall = 74;
            ageBig = 75;
        } else {
            ageSmall = 100;
            ageBig = 101;
        }

        // weight
        int weight;
        int userWeight = user.getWeight();
        if (userWeight < 41) {
            weight = 40;
        } else if (userWeight < 46) {
            weight = 45;
        } else if (userWeight < 51) {
            weight = 50;
        } else if (userWeight < 56) {
            weight = 55;
        } else if (userWeight < 61) {
            weight = 60;
        } else if (userWeight < 66) {
            weight = 65;
        } else if (userWeight < 71) {
            weight = 70;
        } else if (userWeight < 76) {
            weight = 75;
        } else if (userWeight < 81) {
            weight = 80;
        } else if (userWeight < 86) {
            weight = 85;
        } else {
            weight = 90;
        }

        // type
        Type type = typeDAO.findById(user.getActivityType().getId()).get();

        // sex
        String sex;
        int id = user.getGender().getId();
        if (id == 1) {
            sex = "чоловік";
        } else {
            sex = "жінка";
        }

        Set<Norm> byAgeBetweenAndWeightAndSexAndType = normDAO.findByAgeBetweenAndWeightAndSexAndType(ageSmall, ageBig, weight, sex, type);
        if (byAgeBetweenAndWeightAndSexAndType.size() != 0) {

            Set<UserNorm> userNorms = new HashSet<>();
            for (Norm norm : byAgeBetweenAndWeightAndSexAndType) {
                userNorms.add(new UserNorm(new UserNormId(user.getId(), norm.getNutrient().getId()), user, norm.getNutrient(), norm.getQuantity()));
            }

            user.getNorms().addAll(userNorms);
            userDAO.save(user);
        }

        return new ResponseEntity<>(new User_DTO(user), HttpStatus.OK);
    }

    public ResponseEntity<Integer> getRate(int recipeId, int userId) {
        User user = userDAO.findById(userId).get();
        Recipe recipe = recipeDAO.findById(recipeId).get();
        Ranking ranking = rankDAO.findByRecipeAndUser(recipe, user);
        if (ranking != null) {
            return new ResponseEntity<>(ranking.getRanking(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(0, HttpStatus.OK);
        }
    }

    //=================================================================
    private int getTotalPages(long numberOfAllUsers, int pageSize) {
        return (int) Math.ceil((double) numberOfAllUsers / pageSize);
    }

    private long getCount(List<User> users) {
        return users.stream().count();
    }

    private int verifyTo(int to, long numberOfAllUsers) {
        if (to >= numberOfAllUsers) {
            to = (int) numberOfAllUsers;
        }
        return to;
    }

    private List<User_DTO> getChosenDtosFromUsers(int from, int to, List<User> allByParams) {
        return allByParams.stream().map(User_DTO::new).collect(Collectors.toList()).subList(from, to);
    }
}
