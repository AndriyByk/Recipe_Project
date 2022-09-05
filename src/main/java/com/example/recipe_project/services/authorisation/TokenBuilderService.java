package com.example.recipe_project.services.authorisation;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class TokenBuilderService {
    public String createToken(Authentication authResult) {
        return Jwts.builder()
                .setSubject(authResult.getName())
//                .setExpiration(new Date(System.currentTimeMillis() + 5_000))
                .signWith(SignatureAlgorithm.HS512, "ukraine".getBytes())
                .compact();
    }
}
