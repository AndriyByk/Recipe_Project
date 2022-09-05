package com.example.recipe_project.models.entity.entities;

import com.example.recipe_project.models.entity.auth.AuthToken;
import com.example.recipe_project.models.entity.categories.ActivityType;

import com.example.recipe_project.models.entity.categories.Gender;
import com.example.recipe_project.models.entity.categories.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.*;
import java.util.stream.Collectors;

@Entity
@NoArgsConstructor
@Getter
@Setter
@AllArgsConstructor
@Table(name = "users")
public class User implements UserDetails {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private int id;

    @Column(unique = true)
    private String username;
    private String password;
    private String avatar;
    private String email;
    private int weight;
    private int height;
    private String dayOfBirth;

    @ElementCollection(fetch = FetchType.EAGER)
    @Enumerated
//    @Enumerated(EnumType.STRING)
    private List<Role> roles = Arrays.asList(Role.ROLE_USER);

    @ManyToOne(cascade = CascadeType.ALL)
    private Gender gender;

    @ManyToOne(cascade = CascadeType.ALL)
    private ActivityType activityType;

    private String name;
    private String lastName;
    private String dateOfRegistration;

    // Lists
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinTable(name = "favorite_recipes", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "recipe_id"))
    private List<Recipe> favoriteRecipes;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinTable(name = "created_recipes", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "recipe_id"))
    private List<Recipe> createdRecipes;

    // Rank
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Rank> ranks = new ArrayList<>();

//     Token
    @OneToMany(
            fetch = FetchType.EAGER,
            cascade = CascadeType.ALL
//            , orphanRemoval = true
//            , mappedBy = "user"
    )
    private Set<AuthToken> authTokens = new HashSet<>();

    public User(String username, String password, String avatar, String email, int weight, int height, String dayOfBirth, Gender gender, ActivityType activityType, String name, String lastName, String dateOfRegistration) {
        this.username = username;
        this.password = password;
        this.avatar = avatar;
        this.email = email;
        this.weight = weight;
        this.height = height;
        this.dayOfBirth = dayOfBirth;
        this.gender = gender;
        this.activityType = activityType;
        this.name = name;
        this.lastName = lastName;
        this.dateOfRegistration = dateOfRegistration;
    }

    public User(String username, String password, String avatar, String email, int weight, int height, String dayOfBirth, Gender gender, ActivityType activityType, String name, String lastName, String dateOfRegistration, List<Recipe> favoriteRecipes, List<Recipe> createdRecipes, List<Rank> ranks, HashSet<AuthToken> authTokens) {
        this.username = username;
        this.password = password;
        this.avatar = avatar;
        this.email = email;
        this.weight = weight;
        this.height = height;
        this.dayOfBirth = dayOfBirth;
        this.gender = gender;
        this.activityType = activityType;
        this.name = name;
        this.lastName = lastName;
        this.dateOfRegistration = dateOfRegistration;
        this.favoriteRecipes = favoriteRecipes;
        this.createdRecipes = createdRecipes;
        this.ranks = ranks;
        this.authTokens = authTokens;
    }

    public User(int id, String username, String password, String avatar, String email, int weight, int height, String dayOfBirth, Gender gender, ActivityType activityType, String name, String lastName, String dateOfRegistration, HashSet<AuthToken> authTokens) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.avatar = avatar;
        this.email = email;
        this.weight = weight;
        this.height = height;
        this.dayOfBirth = dayOfBirth;
        this.gender = gender;
        this.activityType = activityType;
        this.name = name;
        this.lastName = lastName;
        this.dateOfRegistration = dateOfRegistration;
        this.authTokens = authTokens;
    }

    public User(int id, String username, String password, String avatar, String email, int weight, int height, String dayOfBirth, Gender gender, ActivityType activityType, String name, String lastName, String dateOfRegistration) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.avatar = avatar;
        this.email = email;
        this.weight = weight;
        this.height = height;
        this.dayOfBirth = dayOfBirth;
        this.gender = gender;
        this.activityType = activityType;
        this.name = name;
        this.lastName = lastName;
        this.dateOfRegistration = dateOfRegistration;
    }

    // перевірка ролей під час логінації
    // віддає список ролей того чи іншого користувача
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles.stream().map(role -> new SimpleGrantedAuthority(role.name())).collect(Collectors.toList());
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
