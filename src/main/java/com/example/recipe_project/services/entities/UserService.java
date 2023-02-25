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

//    public ResponseEntity<List<User_DTO>> findAllUsers(int pageNumber, int pageSize) {
//        List<User_DTO> allUsers_dto = userDAO
//                .findAll(PageRequest.of(pageNumber, pageSize)).getContent()
//                .stream().map(User_DTO::new)
//                .collect(Collectors.toList());
//        return new ResponseEntity<>(allUsers_dto, HttpStatus.OK);
//    }

    public ResponseEntity<UserShort_DTO> findUserById(int id) {
        User user = userDAO.findById(id).orElse(new User());
        if (user.getId() == 0) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(new UserShort_DTO(user), HttpStatus.OK);
    }

    //////////////// важливий!!!! але поки прибрав, поки тестив стосунок фейворіт і кріейтед ресайпс

//    не використовується поки
//    public ResponseEntity<User_DTO> updateUserById(int id, User user) {
////        якшо нема такого юзера з таким id то bad request
//        if (userDAO.findAll().stream().allMatch(userDAO -> userDAO.getId() != id)) {
//            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
//        }
////        якшо є - шукаєм в базі
//        User userFromDB = userDAO.findById(id).get();
//
////        почистити норми (в майбутньому їх треба наново порахувати, через зміну даних)
//        userFromDB.getNorms().clear();
//
////        робота з даними
//        if (user.getName() != null)
//            userFromDB.setName(user.getName());
//        if (user.getEmail() != null)
//            userFromDB.setEmail(user.getEmail());
//        if (user.getWeight() != 0)
//            userFromDB.setWeight(user.getWeight());
//        if (user.getDayOfBirth() != null)
//            userFromDB.setDayOfBirth(user.getDayOfBirth());
//        if (user.getGender() != null)
//            userFromDB.setGender(user.getGender());
//        if (user.getActivityType() != null)
//            userFromDB.setActivityType(user.getActivityType());
//        if (user.getLastName() != null)
//            userFromDB.setLastName(user.getLastName());
//
//        // улюблені рецепти - маємо зберегти ті ж самі, які були до того
//        user.setFavoriteRecipes(userFromDB.getFavoriteRecipes());
//
//        // створені рецепти - маємо зберегти ті ж самі, які були до того
//        user.setCreatedRecipes(userFromDB.getCreatedRecipes());
//
//        // оцінки - треба зберегти, які були
//        user.setRankings(userFromDB.getRankings());
//
////      якшо новий рецепт зовсім пустий - bad request
//        if (user.getName() == null &&
//                user.getEmail() == null &&
//                user.getWeight() == 0 &&
//                user.getHeight() == 0 &&
//                user.getDayOfBirth() == null &&
//                user.getGender() == null &&
//                user.getActivityType() == null &&
//                user.getLastName() == null &&
//                user.getRankings() == null &&
//                user.getDateOfRegistration() == null) {
//            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
//        } else {
//            userDAO.save(userFromDB);
//            return new ResponseEntity<>(new User_DTO(userFromDB), HttpStatus.OK);
//        }
//    }

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

//    public ResponseEntity<User_DTO> updateUserByUsername(String username, User user) {
//        if (userDAO.findAll().stream().noneMatch(userDAO -> userDAO.getUsername().equals(username))) {
//            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
//        }
//        User userFromDB = userDAO.findByUsername(username);
//
//        userFromDB.getNorms().clear();
//
//        if (user.getName() != null)
//            userFromDB.setName(user.getName());
//        if (user.getEmail() != null && !user.getEmail().equals(""))
//            userFromDB.setEmail(user.getEmail());
//        if (user.getWeight() != 0)
//            userFromDB.setWeight(user.getWeight());
//        if (user.getDayOfBirth() != null && !user.getDayOfBirth().equals(""))
//            userFromDB.setDayOfBirth(user.getDayOfBirth());
//        if (user.getGender() != null && !user.getGender().getGender().equals(""))
//            userFromDB.setGender(user.getGender());
//        if (user.getActivityType() != null)
//            userFromDB.setActivityType(user.getActivityType());
//        if (user.getLastName() != null && !user.getLastName().equals(""))
//            userFromDB.setLastName(user.getLastName());
////        if (user.getFavoriteRecipes() != null && user.getFavoriteRecipes().size() != 0) {
////            List<FavoriteRecipe> newRecipes = user.getFavoriteRecipes();
////            for (FavoriteRecipe newRecipe : newRecipes) {
////                List<FavoriteRecipe> recipesFromDB = userFromDB.getFavoriteRecipes();
////                for (int j = 0; j < recipesFromDB.size(); j++) {
////                    FavoriteRecipe recipeFromDB = recipesFromDB.get(j);
////                    if (newRecipe.getId() == recipeFromDB.getId()) {
////                        break;
////                    }
////                    if (j == recipesFromDB.size() - 1) {
////                        recipesFromDB.add(newRecipe);
////                    }
////                }
////            }
////        }
////        if (user.getCreatedRecipes() != null && user.getCreatedRecipes().size() != 0) {
////            List<Recipe> newRecipes = user.getCreatedRecipes();
////            for (Recipe newRecipe : newRecipes) {
////                List<Recipe> recipesFromDB = userFromDB.getCreatedRecipes();
////                for (int j = 0; j < recipesFromDB.size(); j++) {
////                    Recipe recipeFromDB = recipesFromDB.get(j);
////                    if (newRecipe.getId() == recipeFromDB.getId()) {
////                        break;
////                    }
////                    if (j == recipesFromDB.size() - 1) {
////                        recipesFromDB.add(newRecipe);
////                    }
////                }
////            }
////        }
////        if (user.getRanks() != null && user.getRanks().size() != 0) {
////            List<Rank> newRanks = user.getRanks();
////            for (Rank newRank : newRanks) {
////                List<Rank> ranksFromDB = userFromDB.getRanks();
////                for (int i = 0; i < ranksFromDB.size(); i++) {
////                    Rank rankFromDB = ranksFromDB.get(i);
////                    if (newRank.getId() == rankFromDB.getId()) {
////                        break;
////                    }
////                    if (i == ranksFromDB.size() - 1) {
////                        ranksFromDB.add(newRank);
////                    }
////                }
////            }
////        }
//
//        if (user.getName() == null &&
//                user.getEmail() == null &&
//                user.getWeight() == 0 &&
//                user.getHeight() == 0 &&
//                user.getDayOfBirth() == null &&
//                user.getGender() == null &&
//                user.getActivityType() == null &&
//                user.getLastName() == null &&
//                user.getRanks() == null &&
//                user.getFavoriteRecipes() == null &&
//                user.getCreatedRecipes() == null &&
//                user.getDateOfRegistration() == null) {
//            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
//        } else {
//            userDAO.save(userFromDB);
//            return new ResponseEntity<>(new User_DTO(userFromDB), HttpStatus.OK);
//        }
//    }

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

    public ResponseEntity<List<User_DTO>> deleteUser(int id) {
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

        AtomicBoolean b = new AtomicBoolean(false);
        favoriteRecipeDAO.findAll().forEach(favoriteRecipe -> {

            if (favoriteRecipe.getRecipe().getId() == recipe.getId()
                    && favoriteRecipe.getUser().getId() == user.getId()) {
                favoriteRecipeDAO.delete(favoriteRecipe);
                System.out.println("favoriteRecipe was deleted");
                b.set(true);
            }
        });
        if (!b.get()) {
            favoriteRecipeDAO.save(new FavoriteRecipe(user, recipe));
            System.out.println("favoriteRecipe was saved");
        }

//        user.getFavoriteRecipes().add(new FavoriteRecipe(recipeDAO.findById(Integer.parseInt(recipeId)).get()));
//        userDAO.save(user);
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

//        System.out.println("---------------------------");
//        System.out.println("ageSmall:  " + ageSmall);
//        System.out.println("ageBig: " + ageBig);
//        System.out.println("---------");
//        System.out.println("Weight  " + weight);
//        System.out.println("Type  " + type.getName());
//        System.out.println("Sex:  " + sex);
//        System.out.println("---------------------------");

        Set<Norm> byAgeBetweenAndWeightAndSexAndType = normDAO.findByAgeBetweenAndWeightAndSexAndType(ageSmall, ageBig, weight, sex, type);
        if (byAgeBetweenAndWeightAndSexAndType.size() != 0) {
//            byAgeBetweenAndWeightAndSexAndType.forEach(norm -> System.out.println(norm.getId()));

            Set<UserNorm> userNorms = new HashSet<>();
            for (Norm norm : byAgeBetweenAndWeightAndSexAndType) {
                userNorms.add(new UserNorm(new UserNormId(user.getId(), norm.getNutrient().getId()), user, norm.getNutrient(), norm.getQuantity()));
            }

            System.out.println("userek will be updated now");
            user.getNorms().addAll(userNorms);

            System.out.println("userek will be saved now");
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
}
