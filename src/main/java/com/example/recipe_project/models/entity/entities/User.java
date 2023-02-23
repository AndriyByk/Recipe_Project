package com.example.recipe_project.models.entity.entities;

import com.example.recipe_project.models.entity.auth.AuthToken;
import com.example.recipe_project.models.entity.categories.user.ActivityType;

import com.example.recipe_project.models.entity.categories.user.Gender;
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

    // @ElementCollection - для ролей створюється окрема табличка = @OneToMany
    @ElementCollection(fetch = FetchType.EAGER)
    @Enumerated
    // @Enumerated = щоб в базу записувало string а не порядковий номер
    //  @Enumerated(EnumType.STRING)
    private List<Role> roles = Arrays.asList(Role.ROLE_USER);

    // Token
    @OneToMany(
            fetch = FetchType.EAGER,
            cascade = CascadeType.ALL
    )
    private Set<AuthToken> authTokens = new HashSet<>();

    @ManyToOne(cascade = CascadeType.ALL)
    private Gender gender;

    @ManyToOne(cascade = CascadeType.ALL)
    private ActivityType activityType;

    private String name;
    private String lastName;
    private String dateOfRegistration;

    // колишній SET
    @OneToMany(
            mappedBy = "user",
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    private List<UserNorm> norms;

    // Rank
    @OneToMany(
            mappedBy = "user",
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    private List<Ranking> rankings;

    // Lists
    @OneToMany(cascade = CascadeType.ALL,
            mappedBy = "user",
            orphanRemoval = true)
    private List<FavoriteRecipe> favoriteRecipes;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "author")
    private List<Recipe> createdRecipes = new ArrayList<>();



    public User(String username, String password, String avatar, String email, int weight, int height, String dayOfBirth, Gender gender, ActivityType activityType, String name, String lastName, String dateOfRegistration, List<FavoriteRecipe> favoriteRecipes, List<Recipe> createdRecipes, List<Ranking> rankings, HashSet<AuthToken> authTokens, List<UserNorm> norms) {
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
        this.rankings = rankings;
        this.authTokens = authTokens;
        this.norms = norms;
    }

    // перевірка ролей під час логінації
    // віддає список ролей того чи іншого користувача
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles.stream().map(role -> new SimpleGrantedAuthority(role.name())).collect(Collectors.toList());
    }

    // створити додаткові поля в юзері і через сетери - зовні їх змінювати і таким чином впливати на поведінку об'єкта
//    в методах відображаються стан полів обєкта, які ми зовні можемо змінювати
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
