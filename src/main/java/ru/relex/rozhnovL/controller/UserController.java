package ru.relex.rozhnovL.controller;

import generator.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.relex.rozhnovL.request.*;
import ru.relex.rozhnovL.Services;
import ru.relex.rozhnovL.entity.User;
import ru.relex.rozhnovL.response.SignUpResponse;


@RestController
public class UserController {

    @Autowired
    Services services;


    /**
     * (USER) Sign Up
     * @param request
     * @return
     */
    @PostMapping(value = "/sign-up", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> signUp(@RequestBody RegistrationRequest request) {
        System.out.println("username: " + request.username + " email: " + request.email);
        String secretKey = SecretKeyGenerator.generateSecretKey(request.username, request.email);

        User newUser = new User(request.username, request.email, secretKey, false);
        services.user.save(newUser);
        return new ResponseEntity<>(new SignUpResponse(secretKey), HttpStatus.CREATED);
    }
}
