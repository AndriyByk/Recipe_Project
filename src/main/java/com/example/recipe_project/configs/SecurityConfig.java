package com.example.recipe_project.configs;

import com.example.recipe_project.dao.entities_dao.IUserDAO;
import com.example.recipe_project.dao.tokens_dao.AuthTokenDAO;
import com.example.recipe_project.services.authorisation.TokenBuilderService;
import com.example.recipe_project.services.entities.UserService;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfigurationSource;

@Configuration
@EnableWebSecurity
@AllArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    private CorsConfigurationSource corsConfigurationSource;
    private DaoAuthenticationProvider daoAuthenticationProvider;
    private UserService userService;
    private TokenBuilderService tokenBuilderService;
    private AuthTokenDAO authTokenDAO;

    // ---3---
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .cors().configurationSource(corsConfigurationSource).and()
                // Cross-site request forgery
                .csrf().disable()
                .authorizeHttpRequests()

                //  будь-яка url: "/*"
                // ----------permitAll()-----------
                .antMatchers(HttpMethod.GET,
                        "/users",
                        "/user/{id}",
                        "/users/activity-types",
                        "/users/genders",
                        "/recipes/allRecipes/{pageNumber}",
                        "/recipes/{id}",
                        "/recipes/find-and-sort/{pageNumber}",
                        "/recipes/categories",
                        "/ingredients",
                        "/ingredients/{id}",
                        "/ingredients/categories",
                        "/nutrients",
                        "/nutrients/{id}",
                        "/nutrients/categories").permitAll()
                .antMatchers(HttpMethod.POST,
                        "/sign-up",
                        "/sign-in").permitAll()

                // -----------hasAnyRole("USER")---------
//                ролі записуються без "ROLE_***"
                .antMatchers(HttpMethod.GET,
                        "/cabinet/{username}",
                        "/users/rates").hasAnyRole("USER","ADMIN","ADMIN_SUPER")
                .antMatchers(HttpMethod.DELETE,
                        "/cabinet/{access}",
                        "/users/{id}" ).hasAnyRole("USER","ADMIN","ADMIN_SUPER")
                .antMatchers(HttpMethod.PATCH,
                        "/users/norms/{username}",
                        "/user/{username}",
                        "/users/update/{username}",
                        "/recipes/{id}",
                        "/recipes/rate").hasAnyRole("USER","ADMIN","ADMIN_SUPER")
                .antMatchers(HttpMethod.POST,
                        "/recipes/{username}").hasAnyRole("USER","ADMIN","ADMIN_SUPER")

                // -----------hasRole("ADMIN")---------
                .antMatchers(HttpMethod.GET, "cabinet/admin").hasRole("ADMIN")
                .antMatchers(HttpMethod.GET, "/allRecipes/admin-mode/{pageNumber}").hasRole("ADMIN")
                .antMatchers(HttpMethod.GET, "/find-and-sort/admin-mode/{pageNumber}").hasRole("ADMIN")

                // -----------hasRole("ADMIN_SUPER")---------
                .antMatchers(HttpMethod.GET, "cabinet/admin-super").hasRole("ADMIN_SUPER")

                // hand-made фільтрація.
                // Щоб збудувався токен, який ми зможемо надалі передавати в різні запити (будується лише раз)
                // то треба хоча б раз, щоб спрацював оцей фільтр (саме тут будується латентний токен)
                .and()
                // коротко про весь фільтр: якшо урла, на яку заходимо є "/sign-in", то будується токен
                // тобто фільтр реагує тільки на урлу "/sign-in"
                // UsernamePasswordAuthenticationFilter - фільтр за умовчанням, вміє працювати тільки з base authentication
                .addFilterBefore(new SignInFilter("/sign-in", authenticationManager(), tokenBuilderService, userService),
                        UsernamePasswordAuthenticationFilter.class)
                // фільтр відхоплює запити, перевіряє чи є токен в запиті, якщо є, то
                // лізе в базу даних і перевіряє чи прив'язаний цей токен до якогось користувача, якщо та, то він буде цього користувача логінити
                .addFilterBefore(new RequestsProcessingFilter(authTokenDAO),
                        UsernamePasswordAuthenticationFilter.class)
                // відключення сесійності, шоб не кешувались запити
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    }

    // ---5---
    // не працює
    // якщо не закоментовано - блокує доступ
    @Override
    public void configure(WebSecurity web) {
//        щоб preflight options request не перекривав наші запити
//        web.ignoring().antMatchers(HttpMethod.OPTIONS, "/**");
    }

    // ---2---
    @Override
    protected void configure(AuthenticationManagerBuilder auth) {
        auth.authenticationProvider(daoAuthenticationProvider);
    }
}
